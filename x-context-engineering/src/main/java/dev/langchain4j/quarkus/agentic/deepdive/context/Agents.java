package dev.langchain4j.quarkus.agentic.deepdive.context;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.ActivationCondition;
import dev.langchain4j.agentic.declarative.ChatMemoryProviderSupplier;
import dev.langchain4j.agentic.declarative.ConditionalAgent;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;

public class Agents {
    public interface MedicalExpertWithMemory {

        @UserMessage("""
            You are a medical expert.
            Analyze the following user request under a medical point of view and provide the best possible answer.
            The user request is {request}.
            """)
        @Agent(description = "A medical expert", outputKey = "response")
        String medical(@MemoryId String memoryId, String request);

        @ChatMemoryProviderSupplier
        static ChatMemory chatMemory(Object memoryId) {
            return MessageWindowChatMemory.withMaxMessages(10);
        }
    }

    public interface LegalExpertWithMemory {

        @UserMessage("""
            You are a legal expert.
            Analyze the following user request under a legal point of view and provide the best possible answer.
            The user request is {request}.
            """)
        @Agent(description = "A legal expert", outputKey = "response", summarizedContext = {"medical", "technical"})
        String legal(@MemoryId String memoryId, String request);

        @ChatMemoryProviderSupplier
        static ChatMemory chatMemory(Object memoryId) {
            return MessageWindowChatMemory.withMaxMessages(10);
        }
    }

    public interface TechnicalExpertWithMemory {

        @UserMessage("""
            You are a technical expert.
            Analyze the following user request under a technical point of view and provide the best possible answer.
            The user request is {request}.
            """)
        @Agent(description = "A technical expert", outputKey = "response")
        String technical(@MemoryId String memoryId, String request);

        @ChatMemoryProviderSupplier
        static ChatMemory chatMemory(Object memoryId) {
            return MessageWindowChatMemory.withMaxMessages(10);
        }
    }

    public enum RequestCategory {
        LEGAL, MEDICAL, TECHNICAL, UNKNOWN
    }

    public interface CategoryRouterWithModel {

        @UserMessage("""
            Analyze the following user request and categorize it as 'legal', 'medical' or 'technical'.
            In case the request doesn't belong to any of those categories categorize it as 'unknown'.
            Reply with only one of those words and nothing else.
            The user request is: '{request}'.
            """)
        @Agent(description = "Categorize a user request", outputKey = "category")
        RequestCategory classify(String request);
    }

    public interface ExpertsAgentWithMemory {

        @ConditionalAgent(outputKey = "response",
                subAgents = { MedicalExpertWithMemory.class, TechnicalExpertWithMemory.class, LegalExpertWithMemory.class })
        String askExpert(String request);

        @ActivationCondition(MedicalExpertWithMemory.class)
        static boolean activateMedical(RequestCategory category) {
            return category == RequestCategory.MEDICAL;
        }

        @ActivationCondition(TechnicalExpertWithMemory.class)
        static boolean activateTechnical(RequestCategory category) {
            return category == RequestCategory.TECHNICAL;
        }

        @ActivationCondition(LegalExpertWithMemory.class)
        static boolean activateLegal(RequestCategory category) {
            return category == RequestCategory.LEGAL;
        }
    }

    public interface ExpertRouterAgentWithMemory extends AgenticScopeAccess {

        @SequenceAgent(outputKey = "response",
                subAgents = { CategoryRouterWithModel.class, ExpertsAgentWithMemory.class })
        String ask(@MemoryId String memoryId, String request);
    }
}
