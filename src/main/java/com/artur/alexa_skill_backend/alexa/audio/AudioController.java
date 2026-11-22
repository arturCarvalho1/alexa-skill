package com.artur.alexa_skill_backend.alexa.audio;

import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioController {

    private final AudioCacheService audioCacheService;

    @GetMapping("/{hash}.mp3")
    public ResponseEntity<byte[]> servirMp3(@PathVariable String hash) {
        return audioCacheService.buscarAudio(hash)
                .map(bytes -> ResponseEntity.ok()
                        .contentType(MediaType.valueOf("audio/mpeg"))
                        .cacheControl(CacheControl.maxAge(1, TimeUnit.HOURS))
                        .body(bytes))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}