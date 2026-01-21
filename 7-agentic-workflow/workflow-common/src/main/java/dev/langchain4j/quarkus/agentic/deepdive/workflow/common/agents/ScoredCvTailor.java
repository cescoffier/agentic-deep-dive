package dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ScoredCvTailor {

    @Agent(description = "Tailors a CV according to specific instructions", outputKey = "cv")
    @SystemMessage("""
            Here is a CV that needs tailoring to a specific job description, feedback or other instruction.
            You can make the CV look good to meet the requirements, but don't invent facts.
            You can drop irrelevant things if it makes the CV better suited to the instructions.
            The goal is that the applicant gets an interview and can then live up to the CV.
            The current CV: {{cv}}
            """)
    @UserMessage("""
            Here are the instructions and feedback for tailoring the CV:
            (Again, do not invent facts that are not part of the original CV.
            If the applicant is not suitable, highlight his existing features
            that match most closely, but do not make up facts)
            The review: {{cvReview}}
            """)
    String tailorCv(@V("cv") String cv, @V("cvReview") CvReview cvReview);
}
