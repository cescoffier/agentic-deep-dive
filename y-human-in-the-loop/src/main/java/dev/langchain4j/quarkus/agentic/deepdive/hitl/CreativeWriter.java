package dev.langchain4j.quarkus.agentic.deepdive.hitl;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CreativeWriter {

    @UserMessage("""
                You are a creative writer.
                Generate a draft of a story long no more than 3 sentence around the given topic.
                Return only the story and nothing else.
                The topic is {{topic}}.
                """)
    @Agent("Generate a story based on the given topic")
    String generateStory(@V("topic") String topic);
}