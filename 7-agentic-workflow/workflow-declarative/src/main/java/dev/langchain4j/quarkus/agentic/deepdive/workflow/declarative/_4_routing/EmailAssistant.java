package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative._4_routing;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import io.quarkiverse.langchain4j.ToolBox;

public interface EmailAssistant {

    @Agent("Sends rejection emails to candidates that didn't pass")
    @SystemMessage("""
            You send a kind email to application candidates that did not pass the first review round.
            You also update the application status to 'rejected'.
            """)
    @UserMessage("""
            Rejected candidate: {{candidateContact}}
            
            For job: {{jobDescription}}
            """)
    @ToolBox(OrganizingTools.class)
    String send(@V("candidateContact") String candidateContact, @V("jobDescription") String jobDescription);
}
