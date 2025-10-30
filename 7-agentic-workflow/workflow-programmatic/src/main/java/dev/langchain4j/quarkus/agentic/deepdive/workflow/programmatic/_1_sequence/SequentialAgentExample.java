package dev.langchain4j.quarkus.agentic.deepdive.workflow.programmatic._1_sequence;

import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvGenerator;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.agents.CvTailor;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.ChatModelProvider;
import dev.langchain4j.quarkus.agentic.deepdive.workflow.common.util.StringLoader;

import java.io.IOException;

public class SequentialAgentExample {

    private static final ChatModel CHAT_MODEL = ChatModelProvider.createChatModel();

    public static void main(String[] args) throws IOException {
        CvGenerator cvGenerator = AgenticServices
                .agentBuilder(CvGenerator.class)
                .chatModel(CHAT_MODEL)
                .outputKey("masterCv")
                .build();
        CvTailor cvTailor = AgenticServices
                .agentBuilder(CvTailor.class)
                .chatModel(CHAT_MODEL)
                .outputKey("tailoredCv")
                .build();

        SequenceCvGenerator sequenceCvGenerator = AgenticServices
                .sequenceBuilder(SequenceCvGenerator.class)
                .subAgents(cvGenerator, cvTailor)
                .outputKey("tailoredCv")
                .build();

        String lifeStory = StringLoader.loadFromResource("/documents/user_life_story.txt");
        String instructions = "Adapt the CV to the job description below." +
                StringLoader.loadFromResource("/documents/job_description_backend.txt");

        String tailoredCv = sequenceCvGenerator.generateTailoredCv(lifeStory, instructions);
        System.out.println(tailoredCv);
    }
}