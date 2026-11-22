package com.artur.alexa_skill_backend.alexa.audio;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class ElevenLabsClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String voiceId;
    private final String modelId;

    public ElevenLabsClient(
            @Value("${elevenlabs.api-key:}") String apiKey,
            @Value("${elevenlabs.voice-id:}") String voiceId,
            @Value("${elevenlabs.model-id:eleven_flash_v2_5}") String modelId
    ) {
        this.apiKey = apiKey;
        this.voiceId = voiceId;
        this.modelId = modelId;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.elevenlabs.io/v1")
                .build();
    }

    public boolean estaConfigurado() {
        return apiKey != null && !apiKey.isBlank()
                && voiceId != null && !voiceId.isBlank();
    }

    public String getVoiceId() {
        return voiceId;
    }

    public byte[] sintetizarMp3(String texto) {
        Map<String, Object> body = Map.of(
                "text", texto,
                "model_id", modelId,
                "voice_settings", Map.of(
                        "stability", 0.65,
                        "similarity_boost", 0.80
                )
        );

        return restClient.post()
                .uri("/text-to-speech/{voiceId}?output_format=mp3_24000_48", voiceId)
                .header("xi-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.valueOf("audio/mpeg"))
                .body(body)
                .retrieve()
                .body(byte[].class);
    }
}