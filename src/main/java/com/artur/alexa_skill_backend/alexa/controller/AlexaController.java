package com.artur.alexa_skill_backend.alexa.controller;

import com.amazon.ask.Skill;
import com.amazon.ask.builder.CustomSkillBuilder;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.util.JacksonSerializer;
import com.artur.alexa_skill_backend.alexa.handler.CustomIntentHandler;
import com.artur.alexa_skill_backend.alexa.handler.LaunchRequestHandler;
import com.artur.alexa_skill_backend.alexa.handler.SessionEndedHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alexa")
public class AlexaController {

    private final Skill skill;
    private final JacksonSerializer serializer = new JacksonSerializer();

    public AlexaController(
            LaunchRequestHandler launchRequestHandler,
            CustomIntentHandler customIntentHandler,
            SessionEndedHandler sessionEndedHandler
    ) {

        this.skill = new CustomSkillBuilder()
                .addRequestHandlers(launchRequestHandler, customIntentHandler, sessionEndedHandler)
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> handleAlexaRequest(@RequestBody String requestJson) {
        RequestEnvelope requestEnvelope = serializer.deserialize(requestJson, RequestEnvelope.class);
        ResponseEnvelope responseEnvelope = skill.invoke(requestEnvelope);
        String responseJson = serializer.serialize(responseEnvelope);

        return ResponseEntity.ok(responseJson);
    }
}