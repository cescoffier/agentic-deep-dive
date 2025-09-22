package dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface CvReviewer {

    @Agent("Reviews a CV according to specific instructions, gives feedback and a score")
    @SystemMessage("""
            You are the hiring manager for this job:
            {{jobDescription}}
            Your review applicant CVs and need to decide who of the many applicants you invite for an on-site interview.
            You give each CV a score and feedback (both the good and the bad things).
            You can ignore things like missing address and placeholders.
            """)
    @UserMessage("""
            Review this CV: {{cv}}
            """)
    CvReview reviewCv(@V("cv") String cv, @V("jobDescription") String jobDescription);
}
