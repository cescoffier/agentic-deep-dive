"""Copyright 2025 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
"""

import asyncio
import json
import traceback  # Import the traceback module

from collections.abc import AsyncIterator
from pprint import pformat

import gradio as gr

from coordinator import (
    root_agent as coordinator_agent,
)
from google.adk.events import Event
from google.adk.runners import Runner
from google.adk.sessions import InMemorySessionService
from google.genai import types


APP_NAME = 'coordinator_app'
USER_ID = 'default_user'
SESSION_ID = 'default_session'

SESSION_SERVICE = InMemorySessionService()
COORDINATOR_AGENT_RUNNER = Runner(
    agent=coordinator_agent,
    app_name=APP_NAME,
    session_service=SESSION_SERVICE,
)


def pretty_format(data, max_width=100):
    """Format data for better readability in markdown."""
    def convert_to_serializable(obj):
        """Convert complex objects to JSON-serializable format."""
        if hasattr(obj, 'model_dump'):
            return obj.model_dump()
        elif hasattr(obj, '__dict__'):
            return {k: convert_to_serializable(v) for k, v in obj.__dict__.items() if not k.startswith('_')}
        elif isinstance(obj, dict):
            return {k: convert_to_serializable(v) for k, v in obj.items()}
        elif isinstance(obj, (list, tuple)):
            return [convert_to_serializable(item) for item in obj]
        elif isinstance(obj, (str, int, float, bool, type(None))):
            return obj
        else:
            return str(obj)

    try:
        serializable_data = convert_to_serializable(data)
        return json.dumps(serializable_data, indent=2, ensure_ascii=False)
    except Exception as e:
        # Fallback to pformat if JSON serialization fails
        try:
            return pformat(data, indent=2, width=max_width)
        except Exception:
            return str(data)


async def get_response_from_agent(
    message: str,
    history: list[gr.ChatMessage],
) -> AsyncIterator[tuple[gr.ChatMessage, str]]:
    """Get response from host agent."""
    interactions_log = ""

    try:
        event_iterator: AsyncIterator[Event] = COORDINATOR_AGENT_RUNNER.run_async(
            user_id=USER_ID,
            session_id=SESSION_ID,
            new_message=types.Content(
                role='user', parts=[types.Part(text=message)]
            ),
        )

        async for event in event_iterator:
            if event.content and event.content.parts:
                for part in event.content.parts:
                    if part.function_call:
                        call_data = part.function_call.model_dump(exclude_none=True)
                        formatted_call = f'```json\n{pretty_format(call_data)}\n```'
                        interaction_text = f'🛠️ **Agent Call: {part.function_call.name}**\n{formatted_call}\n\n'
                        interactions_log += interaction_text
                        yield gr.ChatMessage(
                            role='assistant',
                            content=f'Calling {part.function_call.name}...',
                        ), interactions_log
                    elif part.function_response:
                        response_content = part.function_response.response
                        if (
                            isinstance(response_content, dict)
                            and 'response' in response_content
                        ):
                            formatted_response_data = response_content[
                                'response'
                            ]
                        else:
                            formatted_response_data = response_content
                        formatted_response = f'```json\n{pretty_format(formatted_response_data)}\n```'
                        interaction_text = f'⚡ **Agent Response from {part.function_response.name}**\n{formatted_response}\n\n'
                        interactions_log += interaction_text
                        yield gr.ChatMessage(
                            role='assistant',
                            content='Processing...',
                        ), interactions_log
            if event.is_final_response():
                final_response_text = ''
                if event.content and event.content.parts:
                    final_response_text = ''.join(
                        [p.text for p in event.content.parts if p.text]
                    )
                elif event.actions and event.actions.escalate:
                    final_response_text = f'Agent escalated: {event.error_message or "No specific message."}'
                if final_response_text:
                    interactions_log += f'✅ **Final Response**\n{final_response_text}\n\n'
                    yield gr.ChatMessage(
                        role='assistant', content=final_response_text
                    ), interactions_log
                break
    except Exception as e:
        print(f'Error in get_response_from_agent (Type: {type(e)}): {e}')
        traceback.print_exc()
        error_msg = 'An error occurred while processing your request. Please check the server logs for details.'
        interactions_log += f'❌ **Error**\n{error_msg}\n\n'
        yield gr.ChatMessage(
            role='assistant',
            content=error_msg,
        ), interactions_log


async def main():
    """Main gradio app."""
    print('Creating ADK session...')
    await SESSION_SERVICE.create_session(
        app_name=APP_NAME, user_id=USER_ID, session_id=SESSION_ID
    )
    print('ADK session created successfully.')

    with gr.Blocks(
        theme=gr.themes.Ocean(), title='A2A Host Agent with Logo'
    ) as demo:
        gr.Image(
            'static/a2a.png',
            width=100,
            height=100,
            scale=0,
            show_label=False,
            show_download_button=False,
            container=False,
            show_fullscreen_button=False,
        )

        gr.Markdown('# A2A Host Agent')
        gr.Markdown('This assistant can help you to create content')

        with gr.Row():
            with gr.Column(scale=1):
                chatbot = gr.Chatbot(label='Chat', height=400)
                msg = gr.Textbox(
                    label='Message',
                    placeholder='Type your message here...',
                    lines=2,
                )
                with gr.Row():
                    submit = gr.Button('Submit', variant='primary')
                    clear = gr.Button('Clear')

            with gr.Column(scale=1):
                interactions = gr.Markdown(
                    label='Agent Interactions Log',
                    value='_Waiting for agent interactions..._',
                    height=400,
                )

        async def respond(message, chat_history):
            """Handle user message and update both chat and interactions."""
            chat_history = chat_history or []
            chat_history.append([message, None])

            async for chat_msg, interactions_log in get_response_from_agent(message, []):
                # Update the assistant's response in the last message tuple
                chat_history[-1] = [message, chat_msg.content]
                yield chat_history, interactions_log

        submit.click(
            respond,
            inputs=[msg, chatbot],
            outputs=[chatbot, interactions],
        ).then(
            lambda: '',
            None,
            msg,
        )

        msg.submit(
            respond,
            inputs=[msg, chatbot],
            outputs=[chatbot, interactions],
        ).then(
            lambda: '',
            None,
            msg,
        )

        clear.click(lambda: ([], '_Waiting for agent interactions..._'), None, [chatbot, interactions])

    print('Launching Gradio interface...')
    demo.queue().launch(
        server_name='0.0.0.0',
        server_port=8083,
    )
    print('Gradio application has been shut down.')


if __name__ == '__main__':
    asyncio.run(main())
