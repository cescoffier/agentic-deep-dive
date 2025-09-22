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

    @Inject
    BankTool bankTool;

    @Override
    public int run(String... args) throws Exception {
        bankTool.createAccount("Mario", 1000.0);
        bankTool.createAccount("Georgios", 1000.0);

        String request = "Transfer 100 EUR from Mario's account to Georgios' one";
        String response = supervisorBanker.invoke(request);
        System.out.println(response);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(SupervisorMain.class, args);
    }
}
