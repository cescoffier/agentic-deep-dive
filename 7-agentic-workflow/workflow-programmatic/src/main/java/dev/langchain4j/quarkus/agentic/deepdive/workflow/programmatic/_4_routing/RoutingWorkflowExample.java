package dev.langchain4j.quarkus.agentic.deepdive.workflow.programmatic._4_routing;

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

public class RoutingWorkflowExample {

    private static final ChatModel CHAT_MODEL = ChatModelProvider.createChatModel();

    public static void main(String[] args) throws IOException {

        HrCvReviewer hrCvReviewer = AgenticServices
                .agentBuilder(HrCvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputKey("hrReview")
                .build();

        ManagerCvReviewer managerCvReviewer = AgenticServices
                .agentBuilder(ManagerCvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputKey("managerReview")
                .build();

        TeamMemberCvReviewer teamMemberCvReviewer = AgenticServices
                .agentBuilder(TeamMemberCvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputKey("teamMemberReview")
                .build();

        EmailAssistant emailAssistant = AgenticServices
                .agentBuilder(EmailAssistant.class)
                .chatModel(CHAT_MODEL)
                .tools(new OrganizingTools())
                .build();

        InterviewOrganizer interviewOrganizer = AgenticServices
                .agentBuilder(InterviewOrganizer.class)
                .chatModel(CHAT_MODEL)
                .tools(new OrganizingTools())
                .build();

        // 2. Create parallel review workflow
        UntypedAgent parallelReviewWorkflow = AgenticServices
                .parallelBuilder()
                .subAgents(hrCvReviewer, managerCvReviewer, teamMemberCvReviewer)
                .outputKey("combinedCvReview")
                .output(agenticScope -> {
                    CvReview hrReview = (CvReview) agenticScope.readState("hrReview");
                    CvReview managerReview = (CvReview) agenticScope.readState("managerReview");
                    CvReview teamMemberReview = (CvReview) agenticScope.readState("teamMemberReview");
                    String feedback = String.join("\n",
                            "HR Review: " + hrReview.feedback,
                            "Manager Review: " + managerReview.feedback,
                            "Team Member Review: " + teamMemberReview.feedback
                    );
                    double avgScore = (hrReview.score + managerReview.score + teamMemberReview.score) / 3.0;
                    System.out.println("Final averaged CV Review Score: " + avgScore + "\n");
                    return new CvReview(avgScore, feedback);
                })
                .build();

        // 3. Create conditional workflow for final decision
        UntypedAgent decisionWorkflow = AgenticServices
                .conditionalBuilder()
                .subAgents(agenticScope -> ((CvReview) agenticScope.readState("combinedCvReview")).score >= 0.8, interviewOrganizer)
                .subAgents(agenticScope -> ((CvReview) agenticScope.readState("combinedCvReview")).score < 0.8, emailAssistant)
                .build();

        // 4. Create complete hiring team workflow: Parallel Review → Decision
        HiringTeamWorkflow hiringTeamWorkflow = AgenticServices
                .sequenceBuilder(HiringTeamWorkflow.class)
                .subAgents(parallelReviewWorkflow, decisionWorkflow)
                .build();

        String jobDescription = StringLoader.loadFromResource("/documents/job_description_backend.txt");
        String candidateCv = StringLoader.loadFromResource("/documents/tailored_cv.txt");
        String candidateContact = StringLoader.loadFromResource("/documents/candidate_contact.txt");
        String hrRequirements = StringLoader.loadFromResource("/documents/hr_requirements.txt");
        String phoneInterviewNotes = StringLoader.loadFromResource("/documents/phone_interview_notes.txt");

        // 6. Execute the hiring team workflow
        hiringTeamWorkflow.processApplication(candidateCv, jobDescription, hrRequirements, phoneInterviewNotes, candidateContact);
    }
}