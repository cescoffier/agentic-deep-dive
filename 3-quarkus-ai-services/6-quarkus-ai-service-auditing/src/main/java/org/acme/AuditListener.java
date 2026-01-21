package org.acme;

import dev.langchain4j.observability.api.event.AiServiceCompletedEvent;
import dev.langchain4j.observability.api.event.AiServiceErrorEvent;
import dev.langchain4j.observability.api.event.AiServiceResponseReceivedEvent;
import dev.langchain4j.observability.api.event.AiServiceStartedEvent;
import dev.langchain4j.observability.api.event.ToolExecutedEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import io.quarkus.logging.Log;

@ApplicationScoped
public class AuditListener {
  public void initialMessagesCreated(@Observes AiServiceStartedEvent e) {
    Log.infof(
			"Initial messages:\nsystemMessage: %s\nuserMessage: %s",
	    e.systemMessage(),
	    e.userMessage()
    );
  }

  public void llmInteractionComplete(@Observes AiServiceCompletedEvent e) {
    Log.infof(
			"LLM interaction complete:\nresult: %s",
	    e.result()
    );
  }

  public void llmInteractionFailed(@Observes AiServiceErrorEvent e) {
    Log.infof(
			"LLM interaction failed:\nfailure: %s",
	    e.error().getMessage()
    );
  }

  public void responseFromLLMReceived(@Observes AiServiceResponseReceivedEvent e) {
    Log.infof(
			"Response from LLM received:\nresponse: %s",
      e.response().aiMessage().text()
    );
  }

  public void toolExecuted(@Observes ToolExecutedEvent e) {
    Log.infof(
			"Tool executed:\nrequest: %s(%s)\nresult: %s",
	    e.request().name(),
	    e.request().arguments(),
	    e.resultText()
    );
  }
}
