package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._2_loop;

import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvGenerator;

public interface CvGeneratorAndReviewer {
    @SequenceAgent(outputKey = "cv",
            description = "Generates a CV based on user-provided information and tailored to instructions",
            subAgents = { CvGenerator.class, LoopCvReviewer.class })
    String reviewCv(String lifeStory, String jobDescription);
}
