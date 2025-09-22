package dev.langchain4j.quarkus.agentic.deepdive.workflow.programmatic._1_sequence;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.scope.ResultWithAgenticScope;
import dev.langchain4j.service.V;

import java.util.Map;

public interface SequenceCvGenerator {
    @Agent("Generates a CV based on user-provided information and tailored to instructions")
    String generateTailoredCv(@V("lifeStory") String lifeStory, @V("instructions") String instructions);
}
