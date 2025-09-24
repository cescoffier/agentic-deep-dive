package dev.langchain4j.quarkus.agentic.deepdive.context;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import io.smallrye.common.annotation.Blocking;

import dev.langchain4j.quarkus.agentic.deepdive.context.Agents.*;
import jakarta.inject.Inject;

@WebSocket(path = "/chatbot")
public class ChatBotWebSocket {

    @Inject
    io.quarkus.websockets.next.WebSocketConnection connection;

    private final ExpertRouterAgentWithMemory expertRouter;

    public ChatBotWebSocket(ExpertRouterAgentWithMemory expertRouter) {
        this.expertRouter = expertRouter;
    }

    @OnOpen
    public String onOpen() {
        return "Hello, I am a expert problem solver bot, how can I help?";
    }

    @OnTextMessage
    @Blocking
    public String onMessage(String request) {
        return expertRouter.ask(connection.id(), request);
    }

}
