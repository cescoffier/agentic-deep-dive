package dev.langchain4j.quarkus.agentic.intro;


import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.quarkus.agentic.intro.a2a.IntroductionAgentClient;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;

@QuarkusMain
public class Intro implements QuarkusApplication {


    @Override
    public int run(String... args) throws Exception {
//        introMario();
//        introGeorgios();
//        introClement();
        introStefano();
        return 0;
    }


    private void introMario() {
        var res = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .modelName("gpt-5-nano")
                .build()
                .chat("Who's Mario Fusco (the langchain4j contributor)? Answer in 50 words or less.");

        System.out.println("Mario Fusco: \n" + res);
    }

    @Inject
    ChatModel model;

    private void introGeorgios() {
        var res = model.chat("Who's Georgios Andrianakis (the Quarkus and Quarkus Langchain4J developer)? Answer in 50 words or less.");
        System.out.println("Georgios Andrianakis: \n" + res);
    }

    @Inject
    IntroductionService aiService;

    @ActivateRequestContext
    public void introClement() {
        var res = aiService.introduce("Clement Escoffier (the Quarkus project co-lead)");
        System.out.println("Clement Escoffier: \n" + res);
    }

    @Inject
    IntroductionAgentClient a2aClient;

    @ActivateRequestContext
    public void introStefano() {
        var r = a2aClient.invokeAgent("Who's Stefano Maestri (Wildfly developer)?");
        System.out.println("Stefano Maestri: \n" + r);
    }

    @RegisterAiService
    public interface IntroductionService {

        @UserMessage("Who's {name}? Answer in 50 words or less.")
        String introduce(String name);
    }


    public static void main(String[] args) {
        Quarkus.run(Intro.class, args);
    }
}
