package dev.langchain4j.quarkus.agentic.deepdive.workflow.programmatic._3_parallel;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.HrCvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.ManagerCvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.TeamMemberCvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.ChatModelProvider;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;

import java.io.IOException;
import java.util.Map;

public class ParallelWorkflowExample {

    private static final ChatModel CHAT_MODEL = ChatModelProvider.createChatModel();

    public static void main(String[] args) throws IOException {

        HrCvReviewer hrCvReviewer = AgenticServices.agentBuilder(HrCvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputKey("hrReview")
                .build();

        ManagerCvReviewer managerCvReviewer = AgenticServices.agentBuilder(ManagerCvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputKey("managerReview")
                .build();

        TeamMemberCvReviewer teamMemberCvReviewer = AgenticServices.agentBuilder(TeamMemberCvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputKey("teamMemberReview")
                .build();

        UntypedAgent cvReviewGenerator = AgenticServices.parallelBuilder()
                .subAgents(hrCvReviewer, managerCvReviewer, teamMemberCvReviewer)
                .outputKey("fullCvReview")
                .output(agenticScope -> {
                    // read the outputs of each reviewer from the agentic scope
                    CvReview hrReview = (CvReview) agenticScope.readState("hrReview");
                    CvReview managerReview = (CvReview) agenticScope.readState("managerReview");
                    CvReview teamMemberReview = (CvReview) agenticScope.readState("teamMemberReview");
                    // return a bundled review with averaged score (or any other aggregation you want here)
                    String feedback = String.join("\n",
                            "HR Review: " + hrReview.feedback,
                            "Manager Review: " + managerReview.feedback,
                            "Team Member Review: " + teamMemberReview.feedback
                    );
                    double avgScore = (hrReview.score + managerReview.score + teamMemberReview.score) / 3.0;
                    return new CvReview(avgScore, feedback);
                })
                .build();

        String candidateCv = StringLoader.loadFromResource("/documents/tailored_cv.txt");
        String jobDescription = StringLoader.loadFromResource("/documents/job_description_backend.txt");
        String hrRequirements = StringLoader.loadFromResource("/documents/hr_requirements.txt");
        String phoneInterviewNotes = StringLoader.loadFromResource("/documents/phone_interview_notes.txt");

        Map<String, Object> arguments = Map.of(
                "candidateCv", candidateCv,
                "jobDescription", jobDescription
                ,"hrRequirements", hrRequirements
                ,"phoneInterviewNotes", phoneInterviewNotes
        );

        var review = cvReviewGenerator.invoke(arguments);

        System.out.println("=== REVIEWED CV ===");
        System.out.println(review);
    }
}