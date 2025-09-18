package dev.langchain4j.quarkus.agentic.intro.a2a;

import io.a2a.client.Client;
import io.a2a.client.ClientEvent;
import io.a2a.client.TaskUpdateEvent;
import io.a2a.client.config.ClientConfig;
import io.a2a.client.http.A2ACardResolver;
import io.a2a.client.transport.jsonrpc.JSONRPCTransport;
import io.a2a.client.transport.jsonrpc.JSONRPCTransportConfig;
import io.a2a.spec.A2AClientError;
import io.a2a.spec.A2AClientException;
import io.a2a.spec.AgentCard;
import io.a2a.spec.Message;
import io.a2a.spec.TextPart;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ApplicationScoped
public class IntroductionAgentClient {


    public String invokeAgent(String question) {
        Client client = null;
        try {
            CompletableFuture<String> response = new CompletableFuture<>();
            client = getAgentClient();
            List<BiConsumer<ClientEvent, AgentCard>> callbacks = List.of(
                    (event, card) -> {
                        if (Objects.requireNonNull(event) instanceof TaskUpdateEvent taskUpdate) {
                            switch (taskUpdate.getTask().getStatus().state()) {
                                case FAILED, CANCELED ->
                                        response.completeExceptionally(new A2AClientError("Task failed with status: " + taskUpdate.getTask().getStatus()));
                                case COMPLETED -> {
                                    TextPart tp = (TextPart) taskUpdate.getTask().getArtifacts().getFirst().parts().getFirst();
                                    response.complete(tp.getText());
                                }
                                default -> Log.infof("Task state: " + taskUpdate.getTask().getStatus().state());
                            }
                        } else {
                            Log.infof("Unhandled event type " + event);
                        }
                    }
            );

            var message = new Message.Builder()
                    .role(Message.Role.USER)
                    .parts(new TextPart(question))
                    .build();
            client.sendMessage(message, callbacks, Throwable::printStackTrace, null);

            return response.join();
        } catch (Exception e) {
            throw new RuntimeException("Unable to invoke agent", e);
        } finally {
            closeQuietly(client);
        }
    }

    private void closeQuietly(Client client) {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                // Ignore me.
            }
        }
    }


    public Client getAgentClient() throws A2AClientError, A2AClientException {
        AgentCard agentCard = new A2ACardResolver("http://localhost:8080").getAgentCard();

        ClientConfig clientConfig = new ClientConfig.Builder()
                .setAcceptedOutputModes(List.of("text"))
                .build();


        // Create a handler that will be used for any errors that occur during streaming
        Consumer<Throwable> errorHandler = Throwable::printStackTrace;

        // Create the client using the builder
        Client client = Client
                .builder(agentCard)
                .clientConfig(clientConfig)
                .withTransport(JSONRPCTransport.class, new JSONRPCTransportConfig())
                .streamingErrorHandler(errorHandler)
                .build();
        return client;
    }
}
