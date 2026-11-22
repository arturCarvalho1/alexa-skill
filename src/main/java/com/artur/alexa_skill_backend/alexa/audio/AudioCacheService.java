package com.artur.alexa_skill_backend.alexa.audio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class AudioCacheService {

    private final ElevenLabsClient elevenLabsClient;
    private final Map<String, byte[]> cache = new ConcurrentHashMap<>();

    @Value("${elevenlabs.enabled:true}")
    private boolean enabled;

    public Optional<String> gerarOuObterHash(String texto) {
        if (!enabled || !elevenLabsClient.estaConfigurado()) {
            return Optional.empty();
        }

        String hash = calcularHash(elevenLabsClient.getVoiceId() + ":" + texto);

        if (cache.containsKey(hash)) {
            return Optional.of(hash);
        }

        try {
            byte[] mp3 = elevenLabsClient.sintetizarMp3(texto);
            if (mp3 != null && mp3.length > 0) {
                cache.put(hash, mp3);
                return Optional.of(hash);
            }
        } catch (Exception e) {
            log.error("Falha ao sintetizar áudio no ElevenLabs. Usando fallback Polly.", e);
        }

        return Optional.empty();
    }

    public Optional<byte[]> buscarAudio(String hash) {
        return Optional.ofNullable(cache.get(hash));
    }

    private String calcularHash(String valor) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(valor.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(encoded).substring(0, 16);
        } catch (Exception e) {
            throw new IllegalStateException("Erro ao gerar hash do áudio", e);
        }
    }
}