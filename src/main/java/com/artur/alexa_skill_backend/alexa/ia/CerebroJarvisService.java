package com.artur.alexa_skill_backend.alexa.ia;

import com.artur.alexa_skill_backend.interacao.model.HistoricoInteracao;
import com.artur.alexa_skill_backend.interacao.service.HistoricoInteracaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CerebroJarvisService {

    private static final String MSG_ERRO_IA =
            "Desculpe, senhor. Meus servidores cognitivos estão temporariamente fora do ar.";

    private static final String SYSTEM_PROMPT = """
            Você é o J.A.R.V.I.S., o assistente pessoal de inteligência artificial do Artur.
            Responda sempre em português do Brasil, com tom calmo, inteligente, polido e levemente sarcástico, chamando-o de "senhor" ou "Artur".
            REGRA CRÍTICA: Suas respostas serão convertidas em áudio na Alexa. Seja direto e responda em no máximo 2 ou 3 frases curtas.
            Nunca use markdown, asteriscos, emojis ou listas numeradas.
            """;

    private final GeminiClient geminiClient;
    private final HistoricoInteracaoService historicoService;

    public String responder(String perguntaUsuario) {
        if (perguntaUsuario == null || perguntaUsuario.isBlank()) {
            return "Às suas ordens, senhor. O que deseja saber?";
        }

        if (!geminiClient.estaConfigurado()) {
            return "Senhor, meu módulo de inteligência artificial ainda não recebeu a chave de acesso.";
        }

        try {
            String contexto = montarMemoriaCurtoPrazo();
            String promptCompleto = contexto.isBlank()
                    ? "Artur disse: " + perguntaUsuario
                    : "Histórico recente da conversa:\n" + contexto + "\n\nAgora Artur disse: " + perguntaUsuario;

            return geminiClient.gerarResposta(SYSTEM_PROMPT, promptCompleto);
        } catch (Exception e) {
            log.error("Erro ao consultar o cérebro de IA (Gemini).", e);
            return MSG_ERRO_IA;
        }
    }

    private String montarMemoriaCurtoPrazo() {
        List<HistoricoInteracao> recentes = historicoService.listarRecentes().stream()
                .filter(h -> h.getTextoUsuario() != null && !h.getTextoUsuario().isBlank())
                .filter(h -> h.getRespostaEnviada() != null && !MSG_ERRO_IA.equals(h.getRespostaEnviada()))
                .limit(4)
                .sorted(Comparator.comparing(HistoricoInteracao::getDataHora))
                .toList();

        return recentes.stream()
                .map(h -> "Artur: " + h.getTextoUsuario() + "\nJarvis: " + h.getRespostaEnviada())
                .collect(Collectors.joining("\n"));
    }
}