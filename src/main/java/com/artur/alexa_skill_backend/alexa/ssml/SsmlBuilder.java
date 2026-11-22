package com.artur.alexa_skill_backend.alexa.ssml;

import com.artur.alexa_skill_backend.alexa.audio.AudioCacheService;
import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SsmlBuilder {

    private final AudioCacheService audioCacheService;

    @Value("${app.base-url}")
    private String baseUrl;

    public String construir(String texto, Optional<ConfiguracaoVozResponse> vozAtiva) {
        Optional<String> audioHash = audioCacheService.gerarOuObterHash(texto);
        if (audioHash.isPresent()) {
            String urlAudio = baseUrl.replaceAll("/$", "") + "/api/audio/" + audioHash.get() + ".mp3";
            return "<speak><audio src=\"" + urlAudio + "\"/></speak>";
        }

        // Fallback automático para Amazon Polly
        String textoSeguro = escaparXml(texto);

        if (vozAtiva.isEmpty()) {
            return "<speak>" + textoSeguro + "</speak>";
        }

        ConfiguracaoVozResponse voz = vozAtiva.get();
        return String.format(
                "<speak><voice name=\"%s\"><prosody rate=\"%s\" pitch=\"%s\">%s</prosody></voice></speak>",
                voz.vozPolly().getNomeSslm(),
                voz.velocidade(),
                voz.tom(),
                textoSeguro
        );
    }

    private String escaparXml(String texto) {
        if (texto == null) {
            return "";
        }
        return texto
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}