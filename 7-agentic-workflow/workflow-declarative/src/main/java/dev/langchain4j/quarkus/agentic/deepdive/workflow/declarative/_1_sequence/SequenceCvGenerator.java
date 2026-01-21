package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._1_sequence;

import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvGenerator;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvTailor;

public interface SequenceCvGenerator {
    @SequenceAgent(outputKey = "tailoredCv",
            description = "Generates a CV based on user-provided information and tailored to instructions",
            subAgents = { CvGenerator.class, CvTailor.class })
    String generateTailoredCv(String lifeStory, String instructions);
}
