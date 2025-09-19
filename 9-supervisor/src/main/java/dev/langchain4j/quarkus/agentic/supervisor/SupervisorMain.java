package dev.langchain4j.quarkus.agentic.supervisor;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

import dev.langchain4j.quarkus.agentic.supervisor.Agents.*;

@QuarkusMain
public class SupervisorMain implements QuarkusApplication {

    @Inject
    SupervisorBanker supervisorBanker;

    @Override
    public int run(String... args) throws Exception {
//        System.out.println("How can I help you?");
//        System.out.print("> ");
//        String request = System.console().readLine();
        String request = "Transfer 100 EUR from Mario's account to Georgios' one";
        String response = supervisorBanker.invoke(request);
        System.out.println(response);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(SupervisorMain.class, args);
    }
}
