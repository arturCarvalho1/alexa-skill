package com.artur.alexa_skill_backend.comando.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "regras_comando")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegraComando {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "intent_name", nullable = false, unique = true, length = 100)
    private String intentName;

    @Column(nullable = false, length = 150)
    private String gatilho;

    @Column(name = "template_resposta", nullable = false, columnDefinition = "TEXT")
    private String templateResposta;

    @Column(nullable = false)
    @Builder.Default
    private boolean ativo = true;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    void prePersist() {
        LocalDateTime agora = LocalDateTime.now();
        this.criadoEm = agora;
        this.atualizadoEm = agora;
    }

    @PreUpdate
    void preUpdate() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
