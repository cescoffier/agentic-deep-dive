package dev.langchain4j.quarkus.agentic.deepdive.workflow.declarative;

import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;
import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

import java.io.IOException;

@QuarkusMain
public class SequentialAgentExample implements QuarkusApplication {

    @Inject
    SequenceCvGenerator sequenceCvGenerator;

    @Override
    public int run(String... args) throws IOException {
        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");
        String instructions = "Adapt the CV to the job description below." +
                StringLoader.loadFromResource("/documents/job_description_backend.txt");

        String tailoredCv = sequenceCvGenerator.generateTailoredCv(lifeStory, instructions);
        System.out.println(tailoredCv);
        return 0;
    }

    public static void main(String[] args) {
        Quarkus.run(SequentialAgentExample.class, args);
    }
}