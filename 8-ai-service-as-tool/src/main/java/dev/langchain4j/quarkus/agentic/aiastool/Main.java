package dev.langchain4j.quarkus.agentic.aiastool;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

@QuarkusMain
public class Main implements QuarkusApplication {

    @Inject
    AssistantAgent assistantAgent;

    @Override
    public int run(String... args) throws Exception {
//        System.out.println("How can I help you?");
//        System.out.print("> ");
//        String request = System.console().readLine();
        String request = "Plan a day in Antwerp and provide the response in French";
        String response = assistantAgent.assist(request);
        System.out.println(response);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(Main.class, args);
    }
}
