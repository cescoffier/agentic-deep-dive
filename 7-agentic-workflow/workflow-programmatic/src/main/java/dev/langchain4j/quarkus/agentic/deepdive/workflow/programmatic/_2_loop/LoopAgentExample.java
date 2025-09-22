package dev.langchain4j.quarkus.agentic.deepdive.workflow.programmatic._2_loop;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvGenerator;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.ScoredCvTailor;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.ChatModelProvider;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;

import java.io.IOException;

public class LoopAgentExample {

    private static final ChatModel CHAT_MODEL = ChatModelProvider.createChatModel();

    public static void main(String[] args) throws IOException {
        CvGenerator cvGenerator = AgenticServices
                .agentBuilder(CvGenerator.class)
                .chatModel(CHAT_MODEL)
                .outputName("cv")
                .build();
        CvReviewer cvReviewer = AgenticServices.agentBuilder(CvReviewer.class)
                .chatModel(CHAT_MODEL)
                .outputName("cvReview")
                .build();
        ScoredCvTailor scoredCvTailor = AgenticServices.agentBuilder(ScoredCvTailor.class)
                .chatModel(CHAT_MODEL)
                .outputName("cv")
                .build();

        UntypedAgent cdReviewerLoop = AgenticServices.loopBuilder()
                .subAgents(cvReviewer, scoredCvTailor)
                .outputName("cv") // this is the final output we want to observe (the improved CV)
                .exitCondition(agenticScope -> {
                            CvReview review = (CvReview) agenticScope.readState("cvReview");
                            System.out.println("Checking exit condition with score = " + review.score);
                            return review.score > 0.8;
                        })
                .maxIterations(3)
                .build();

        LoopCvReviewer reviewedCvGenerator = AgenticServices.sequenceBuilder(LoopCvReviewer.class)
                .subAgents(cvGenerator, cdReviewerLoop)
                .outputName("cv")
                .build();

        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");
        String jobDescription = StringLoader.loadFromResource("/documents/job_description_backend.txt");

        String tailoredCv = reviewedCvGenerator.reviewCv(lifeStory, jobDescription);

        System.out.println("=== REVIEWED CV ===");
        System.out.println(tailoredCv);
    }
}
