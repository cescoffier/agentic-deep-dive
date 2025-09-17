package dev.langchain4j.quarkus.agentic.deepdive.context;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.declarative.ActivationCondition;
import dev.langchain4j.agentic.declarative.ChatMemoryProviderSupplier;
import dev.langchain4j.agentic.declarative.ConditionalAgent;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.agentic.declarative.SubAgent;
import dev.langchain4j.agentic.scope.AgenticScopeAccess;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class Agents {
    public interface MedicalExpertWithMemory {

        @UserMessage("""
            You are a medical expert.
            Analyze the following user request under a medical point of view and provide the best possible answer.
            The user request is {{request}}.
            """)
        @Tool("A medical expert")
        @Agent("A medical expert")
        String medical(@MemoryId String memoryId, @V("request") String request);

        @ChatMemoryProviderSupplier
        static ChatMemory chatMemory(Object memoryId) {
            return MessageWindowChatMemory.withMaxMessages(10);
        }
    }

    public interface LegalExpertWithMemory {

        @UserMessage("""
            You are a legal expert.
            Analyze the following user request under a legal point of view and provide the best possible answer.
            The user request is {{request}}.
            """)
        @Tool("A legal expert")
        @Agent("A legal expert")
        String legal(@MemoryId String memoryId, @V("request") String request);

        @ChatMemoryProviderSupplier
        static ChatMemory chatMemory(Object memoryId) {
            return MessageWindowChatMemory.withMaxMessages(10);
        }
    }

    public interface TechnicalExpertWithMemory {

        @UserMessage("""
            You are a technical expert.
            Analyze the following user request under a technical point of view and provide the best possible answer.
            The user request is {{request}}.
            """)
        @Tool("A technical expert")
        @Agent("A technical expert")
        String technical(@MemoryId String memoryId, @V("request") String request);

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
            The user request is: '{{request}}'.
            """)
        @Agent("Categorize a user request")
        RequestCategory classify(@V("request") String request);
    }

    public interface ExpertsAgentWithMemory {

        @ConditionalAgent(outputName = "response", subAgents = {
                @SubAgent(type = MedicalExpertWithMemory.class, outputName = "response"),
                @SubAgent(type = TechnicalExpertWithMemory.class, outputName = "response"),
                @SubAgent(type = LegalExpertWithMemory.class, outputName = "response", summarizedContext = {"medical", "technical"})
        })
        String askExpert(@V("request") String request);

        @ActivationCondition(MedicalExpertWithMemory.class)
        static boolean activateMedical(@V("category") RequestCategory category) {
            return category == RequestCategory.MEDICAL;
        }

        @ActivationCondition(TechnicalExpertWithMemory.class)
        static boolean activateTechnical(@V("category") RequestCategory category) {
            return category == RequestCategory.TECHNICAL;
        }

        @ActivationCondition(LegalExpertWithMemory.class)
        static boolean activateLegal(@V("category") RequestCategory category) {
            return category == RequestCategory.LEGAL;
        }
    }

    public interface ExpertRouterAgentWithMemory extends AgenticScopeAccess {

        @SequenceAgent(outputName = "response", subAgents = {
                @SubAgent(type = CategoryRouterWithModel.class, outputName = "category"),
                @SubAgent(type = ExpertsAgentWithMemory.class, outputName = "response")
        })
        String ask(@MemoryId String memoryId, @V("request") String request);
    }
}
