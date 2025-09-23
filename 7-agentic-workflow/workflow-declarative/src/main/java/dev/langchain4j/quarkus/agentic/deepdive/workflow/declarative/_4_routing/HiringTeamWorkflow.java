package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._4_routing;

import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.domain.CvReview;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._3_parallel.ParallelCvReviewer;
import dev.langchain4j.service.V;

public interface HiringTeamWorkflow {
    @SequenceAgent(outputName = "combinedCvReview",
            description = "Based on CV, phone interview and job description, this agent will either invite or reject the candidate",
            subAgents = {
                    @SubAgent(type = ParallelCvReviewer.class, outputName = "combinedCvReview"),
                    @SubAgent(type = CandidateRouter.class)
            })
    CvReview processApplication(@V("candidateCv") String candidateCv,
                                @V("jobDescription") String jobDescription,
                                @V("hrRequirements") String hrRequirements,
                                @V("phoneInterviewNotes") String phoneInterviewNotes,
                                @V("candidateContact") String candidateContact);
}
