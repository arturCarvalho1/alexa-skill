package com.artur.alexa_skill_backend.voz.dto;

import com.artur.alexa_skill_backend.voz.model.VozPolly;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConfiguracaoVozRequest(
        @NotBlank(message = "O nome do perfil é obrigatório")
        @Size(max = 80, message = "Máximo de 80 caracteres")
        String nomePerfil,

        @NotNull(message = "A voz Polly é obrigatória")
        VozPolly vozPolly,

        @Size(max = 20)
        String velocidade,

        @Size(max = 20)
        String tom,

        Boolean ativarAgora
) {}