package dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ManagerCvReviewer {

    @Agent(name = "managerReviewer", outputKey = "managerReview",
            description = "Reviews a CV based on a job description, gives feedback and a score")
    @SystemMessage("""
            You are the hiring manager for this job:
            {{jobDescription}}
            Your review applicant CVs and need to decide who of the many applicants you invite for an on-site interview.
            You give each CV a score and feedback (both the good and the bad things).
            You can ignore things like missing address and placeholders.
            
            IMPORTANT: Return your response as valid JSON only, without any markdown formatting or code blocks.
            """)
    @UserMessage("""
            Review this CV: {{candidateCv}}
            """)
    CvReview reviewCv(@V("candidateCv") String cv, @V("jobDescription") String jobDescription);
}
