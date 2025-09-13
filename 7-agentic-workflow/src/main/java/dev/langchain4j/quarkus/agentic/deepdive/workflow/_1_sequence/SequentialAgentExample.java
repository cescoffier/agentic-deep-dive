package dev.langchain4j.quarkus.agentic.deepdive.workflow._1_sequence;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.agents.CvGenerator;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.agents.CvTailor;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.util.ChatModelProvider;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.util.StringLoader;

import java.io.IOException;

public class SequentialAgentExample {

    private static final ChatModel CHAT_MODEL = ChatModelProvider.createChatModel();

    public static void main(String[] args) throws IOException {
        CvGenerator cvGenerator = AgenticServices
                .agentBuilder(CvGenerator.class)
                .chatModel(CHAT_MODEL)
                .outputName("masterCv")
                .build();
        CvTailor cvTailor = AgenticServices
                .agentBuilder(CvTailor.class)
                .chatModel(CHAT_MODEL)
                .outputName("tailoredCv")
                .build();

        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");
        String instructions = "Adapt the CV to the job description below." + StringLoader.loadFromResource("/documents/job_description_backend.txt");

        SequenceCvGenerator sequenceCvGenerator = AgenticServices.sequenceBuilder(SequenceCvGenerator.class)
                .subAgents(cvGenerator, cvTailor)
                .outputName("tailoredCv")
                .build();

        String tailoredCv = sequenceCvGenerator.generateTailoredCv(lifeStory, instructions);
        System.out.println(tailoredCv);
    }
}