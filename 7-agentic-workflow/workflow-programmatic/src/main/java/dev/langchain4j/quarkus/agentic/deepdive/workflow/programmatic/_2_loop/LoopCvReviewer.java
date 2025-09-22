package dev.langchain4j.quarkus.agentic.deepdive.workflow.programmatic._2_loop;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.V;

public interface LoopCvReviewer {
    @Agent("Generates a CV based on user-provided information and tailored to instructions")
    String reviewCv(@V("lifeStory") String lifeStory, @V("jobDescription") String jobDescription);
}
