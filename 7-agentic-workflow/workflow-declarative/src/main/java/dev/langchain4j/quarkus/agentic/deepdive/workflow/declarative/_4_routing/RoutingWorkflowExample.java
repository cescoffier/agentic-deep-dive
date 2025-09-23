package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._4_routing;

import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import jakarta.inject.Inject;

import java.io.IOException;

public class RoutingWorkflowExample implements QuarkusApplication {

    @Inject
    HiringTeamWorkflow hiringTeamWorkflow;

    @Override
    public int run(String... args) throws IOException {

        String jobDescription = StringLoader.loadFromResource("/documents/job_description_backend.txt");
        String candidateCv = StringLoader.loadFromResource("/documents/tailored_cv.txt");
        String candidateContact = StringLoader.loadFromResource("/documents/candidate_contact.txt");
        String hrRequirements = StringLoader.loadFromResource("/documents/hr_requirements.txt");
        String phoneInterviewNotes = StringLoader.loadFromResource("/documents/phone_interview_notes.txt");

        hiringTeamWorkflow.processApplication(candidateCv, jobDescription, hrRequirements, phoneInterviewNotes, candidateContact);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(RoutingWorkflowExample.class, args);
    }
}