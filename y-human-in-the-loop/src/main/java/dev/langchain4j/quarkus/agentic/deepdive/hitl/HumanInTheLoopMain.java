package dev.langchain4j.quarkus.agentic.deepdive.hitl;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.workflow.HumanInTheLoop;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;

import static dev.langchain4j.model.openai.OpenAiChatModelName.GPT_4_O_MINI;

public class HumanInTheLoopMain {

    private static final ChatModel CHAT_MODEL = createChatModel();

    public static void main(String[] args) {
        CreativeWriter creativeWriter = AgenticServices.agentBuilder(CreativeWriter.class)
                .chatModel(CHAT_MODEL)
                .outputKey("story")
                .build();

        AudienceEditor audienceEditor = AgenticServices.agentBuilder(AudienceEditor.class)
                .chatModel(CHAT_MODEL)
                .outputKey("story")
                .build();

        HumanInTheLoop humanInTheLoop = AgenticServices.humanInTheLoopBuilder()
                .description("An agent that asks the audience for the story")
                .inputKey("topic")
                .outputKey("audience")
//                .async(true)
                .requestWriter(q -> {
                    System.out.println("Which audience for topic " + q + "?");
                    System.out.print("> ");
                })
                .responseReader(() -> {
                    try {
                        return new BufferedReader(new InputStreamReader(System.in)).readLine();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();

        UntypedAgent novelCreator = AgenticServices.sequenceBuilder()
                .subAgents(
                        humanInTheLoop, // asks user for the audience in a non-blocking way
                        creativeWriter, // doesn't need the audience so it can generate the story without waiting for the human-in-the-loop response
                        audienceEditor) // use the audience provided by the human-in-the-loop
                .outputKey("story")
                .build();

        Map<String, Object> input = Map.of(
                "topic", "dragons and wizards"
        );

        String story = (String) novelCreator.invoke(input);
        System.out.println(story);
    }

    public static ChatModel createChatModel() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName(GPT_4_O_MINI)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
