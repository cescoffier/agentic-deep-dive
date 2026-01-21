package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._2_loop;

import dev.langchain4j.agentic.declarative.ExitCondition;
import dev.langchain4j.agentic.declarative.LoopAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvReviewer;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.ScoredCvTailor;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;

public interface LoopCvReviewer {
    @LoopAgent(
            description = "Review and score the given cv",
            outputKey = "cv", maxIterations = 3,
            subAgents = { CvReviewer.class, ScoredCvTailor.class }
    )
    String reviewAndScore(String cv, String jobDescription);

    @ExitCondition
    static boolean exit(CvReview cvReview) {
        System.out.println("Checking exit condition with score = " + cvReview.score);
        return cvReview.score > 0.8;
    }
}
