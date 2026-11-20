package com.artur.alexa_skill_backend.voz.dto;

import com.artur.alexa_skill_backend.voz.model.ConfiguracaoVoz;
import com.artur.alexa_skill_backend.voz.model.VozPolly;

import java.time.LocalDateTime;

public record ConfiguracaoVozResponse(
        Long id,
        String nomePerfil,
        VozPolly vozPolly,
        String velocidade,
        String tom,
        boolean ativa,
        LocalDateTime criadoEm
) {
    public static ConfiguracaoVozResponse fromEntity(ConfiguracaoVoz entity) {
        return new ConfiguracaoVozResponse(
                entity.getId(),
                entity.getNomePerfil(),
                entity.getVozPolly(),
                entity.getVelocidade(),
                entity.getTom(),
                entity.isAtiva(),
                entity.getCriadoEm()
        );
    }
}