package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._3_parallel;

import dev.langchain4j.agentic.declarative.Output;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.HrCvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.ManagerCvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.TeamMemberCvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;

public interface ParallelCvReviewer {
    @ParallelAgent(outputKey = "combinedCvReview",
            description = "Submit the provided CV to multiple reviewers in parallel and aggregate their feedback",
            subAgents = {
                    @SubAgent(type = HrCvReviewer.class, outputKey = "hrReview"),
                    @SubAgent(type = ManagerCvReviewer.class, outputKey = "managerReview"),
                    @SubAgent(type = TeamMemberCvReviewer.class, outputKey = "teamMemberReview")
            })
    CvReview review(String candidateCv, String jobDescription, String hrRequirements, String phoneInterviewNotes);

    @Output
    static CvReview aggregateReviews(CvReview hrReview, CvReview managerReview, CvReview teamMemberReview) {
        String feedback = String.join("\n",
                "HR Review: " + hrReview.feedback,
                "Manager Review: " + managerReview.feedback,
                "Team Member Review: " + teamMemberReview.feedback
        );
        double avgScore = (hrReview.score + managerReview.score + teamMemberReview.score) / 3.0;
        return new CvReview(avgScore, feedback);
    }
}
