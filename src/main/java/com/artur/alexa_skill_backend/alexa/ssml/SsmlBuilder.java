package com.artur.alexa_skill_backend.alexa.ssml;

import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozResponse;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SsmlBuilder {

    public String construir(String texto, Optional<ConfiguracaoVozResponse> vozAtiva) {
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