package org.acme;

import dev.langchain4j.guardrail.OutputGuardrail;
import dev.langchain4j.guardrail.OutputGuardrailRequest;
import dev.langchain4j.guardrail.OutputGuardrailResult;
import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.logging.Log;

@ApplicationScoped
public class UppercaseOutputGuardrail implements OutputGuardrail {

    @Override
    public OutputGuardrailResult validate(OutputGuardrailRequest request) {
        Log.infof("Response is: %s", request.responseFromLLM().aiMessage().text());

        var message = request.responseFromLLM().aiMessage().text();
        var isAllUppercase = message.chars()
                                    .filter(Character::isLetter)
                                    .allMatch(Character::isUpperCase);

        if (isAllUppercase) {
            Log.info("success");
            return success();
        } else {
            return reprompt("The output must be in uppercase.", "Please provide the output in uppercase.");
        }
    }

}
