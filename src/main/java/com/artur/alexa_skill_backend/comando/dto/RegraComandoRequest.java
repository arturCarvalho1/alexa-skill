package com.artur.alexa_skill_backend.comando.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegraComandoRequest(
    @NotBlank(message = "O nome da intent é obrigatório")
    @Size(max = 100)
    String intentName,

    @NotBlank(message = "O gatilho é obrigatório")
    @Size(max = 150)
    String gatilho,

    @NotBlank(message = "O template de resposta é obrigatório")
    String templateResposta,

    Boolean ativo
) {}

