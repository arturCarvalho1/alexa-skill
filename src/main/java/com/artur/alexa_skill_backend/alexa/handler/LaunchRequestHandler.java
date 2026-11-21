package com.artur.alexa_skill_backend.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
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
public class LaunchRequestHandler implements RequestHandler {

    private final ConfiguracaoVozService vozService;
    private final RegraComandoService comandoService;
    private final HistoricoInteracaoService historicoService;
    private final SsmlBuilder ssmlBuilder;

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String respostaTexto = comandoService.buscarRespostaParaIntent("LaunchRequest");
        Optional<ConfiguracaoVozResponse> vozAtiva = vozService.buscarAtivaOpcional();

        String ssml = ssmlBuilder.construir(respostaTexto, vozAtiva);
        String nomeVoz = vozAtiva.map(v -> v.vozPolly().getNomeSslm()).orElse("PADRAO_ALEXA");

        historicoService.registrar("LaunchRequest", "Abertura da Skill", respostaTexto, nomeVoz);

        return input.getResponseBuilder()
                .withSpeech(ssml)
                .withReprompt(ssml)
                .withShouldEndSession(false)
                .build();
    }
}
