package com.artur.alexa_skill_backend.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import com.artur.alexa_skill_backend.alexa.ssml.SsmlBuilder;
import com.artur.alexa_skill_backend.comando.service.RegraComandoService;
import com.artur.alexa_skill_backend.interacao.service.HistoricoInteracaoService;
import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozResponse;
import com.artur.alexa_skill_backend.voz.service.ConfiguracaoVozService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomIntentHandler implements RequestHandler {

    private final ConfiguracaoVozService vozService;
    private final RegraComandoService comandoService;
    private final HistoricoInteracaoService historicoService;
    private final SsmlBuilder ssmlBuilder;

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(IntentRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        String intentName = intentRequest.getIntent().getName();

        Optional<ConfiguracaoVozResponse> vozAtiva = vozService.buscarAtivaOpcional();
        String nomeVoz = vozAtiva.map(v -> v.vozPolly().getNomeSslm()).orElse("PADRAO_ALEXA");

        if ("AMAZON.StopIntent".equals(intentName) || "AMAZON.CancelIntent".equals(intentName)) {
            String despedida = "Tchau vagabundo, não me procure mais";
            String ssml = ssmlBuilder.construir(despedida, vozAtiva);
            historicoService.registrar(intentName, "Encerramento", despedida, nomeVoz);

            return input.getResponseBuilder()
                    .withSpeech(ssml)
                    .withShouldEndSession(true)
                    .build();
        }

        String respostaTexto = comandoService.buscarRespostaParaIntent(intentName);
        String ssml = ssmlBuilder.construir(respostaTexto, vozAtiva);

        historicoService.registrar(intentName, intentName, respostaTexto, nomeVoz);

        return input.getResponseBuilder()
                .withSpeech(ssml)
                .withShouldEndSession(false)
                .build();
    }
}