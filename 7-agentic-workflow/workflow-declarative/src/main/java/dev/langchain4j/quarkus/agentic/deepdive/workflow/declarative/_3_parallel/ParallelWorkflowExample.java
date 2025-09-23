package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._3_parallel;

import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.inject.Inject;

import java.io.IOException;

public class ParallelWorkflowExample implements QuarkusApplication {

    @Inject
    ParallelCvReviewer parallelCvReviewer;

    @Override
    public int run(String... args) throws IOException {

        String candidateCv = StringLoader.loadFromResource("/documents/tailored_cv.txt");
        String jobDescription = StringLoader.loadFromResource("/documents/job_description_backend.txt");
        String hrRequirements = StringLoader.loadFromResource("/documents/hr_requirements.txt");
        String phoneInterviewNotes = StringLoader.loadFromResource("/documents/phone_interview_notes.txt");

        CvReview review = parallelCvReviewer.review(candidateCv, jobDescription, hrRequirements, phoneInterviewNotes);

        System.out.println("=== REVIEWED CV ===");
        System.out.println(review);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(ParallelWorkflowExample.class, args);
    }
}