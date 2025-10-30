package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._2_loop;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvGenerator;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvTailor;
import dev.langchain4j.service.V;

public interface CvGeneratorAndReviewer {
    @SequenceAgent(outputKey = "cv",
            description = "Generates a CV based on user-provided information and tailored to instructions",
            subAgents = {
                    @SubAgent(type = CvGenerator.class, outputKey = "cv"),
                    @SubAgent(type = LoopCvReviewer.class, outputKey = "cv")
            })
    String reviewCv(String lifeStory, String jobDescription);
}
