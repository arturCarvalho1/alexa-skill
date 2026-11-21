package com.artur.alexa_skill_backend.comando.dto;

import com.artur.alexa_skill_backend.comando.model.RegraComando;

import java.time.LocalDateTime;

public record RegraComandoResponse(
        Long id,
        String intentName,
        String gatilho,
        String templateResposta,
        boolean ativo,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
    public static RegraComandoResponse fromEntity(RegraComando entity){
        return new RegraComandoResponse(
                entity.getId(),
                entity.getIntentName(),
                entity.getGatilho(),
                entity.getTemplateResposta(),
                entity.isAtivo(),
                entity.getCriadoEm(),
                entity.getAtualizadoEm()
        );
    }
}
