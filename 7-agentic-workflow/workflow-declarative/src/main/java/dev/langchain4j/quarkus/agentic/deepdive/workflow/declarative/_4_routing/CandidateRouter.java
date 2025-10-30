package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._4_routing;

import dev.langchain4j.agentic.declarative.ActivationCondition;
import dev.langchain4j.agentic.declarative.ConditionalAgent;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;

public interface CandidateRouter {
    @ConditionalAgent(outputKey = "combinedCvReview",
            description = "Submit the provided CV to multiple reviewers in parallel and aggregate their feedback",
            subAgents = {
                    @SubAgent(type = EmailAssistant.class),
                    @SubAgent(type = InterviewOrganizer.class)
            })
    CvReview review(CvReview combinedCvReview);

    @ActivationCondition(EmailAssistant.class)
    static boolean sendEmail(CvReview combinedCvReview) {
        return combinedCvReview.score < 0.8;
    }

    @ActivationCondition(InterviewOrganizer.class)
    static boolean organizeInterview(CvReview combinedCvReview) {
        return combinedCvReview.score >= 0.8;
    }
}
