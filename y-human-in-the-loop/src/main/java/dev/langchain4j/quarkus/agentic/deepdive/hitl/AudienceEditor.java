package dev.langchain4j.quarkus.agentic.deepdive.hitl;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface AudienceEditor {

    @UserMessage("""
            You are a professional editor.
            Analyze and rewrite the following story to better align with the target audience of {{audience}}.
            Return only the story and nothing else.
            The story is "{{story}}".
            """)
    @Agent("Edit a story to better fit a given audience")
    String editStory(@V("story") String story, @V("audience") String audience);
}