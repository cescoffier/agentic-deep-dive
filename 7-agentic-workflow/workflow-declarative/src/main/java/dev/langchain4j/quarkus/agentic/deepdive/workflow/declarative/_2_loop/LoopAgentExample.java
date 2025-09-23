package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._2_loop;

import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.inject.Inject;

import java.io.IOException;

public class LoopAgentExample implements QuarkusApplication {

    @Inject
    CvGeneratorAndReviewer cvGeneratorAndReviewer;

    @Override
    public int run(String... args) throws IOException {
        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");
        String jobDescription = StringLoader.loadFromResource("/documents/job_description_backend.txt");

        String tailoredCv = cvGeneratorAndReviewer.reviewCv(lifeStory, jobDescription);

        System.out.println("=== REVIEWED CV ===");
        System.out.println(tailoredCv);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(LoopAgentExample.class, args);
    }
}