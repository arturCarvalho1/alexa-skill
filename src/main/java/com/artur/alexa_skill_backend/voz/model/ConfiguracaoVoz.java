package com.artur.alexa_skill_backend.voz.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "configuracoes_voz")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConfiguracaoVoz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_perfil", nullable = false, unique = true, length = 80)
    private String nomePerfil;

    @Enumerated(EnumType.STRING)
    @Column(name = "voz_polly", nullable = false, length = 30)
    private VozPolly vozPolly;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String velocidade = "medium"; // ex: x-slow, slow, medium, fast, 110%

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String tom = "medium"; // ex: -10%, medium, +5%

    @Column(nullable = false)
    @Builder.Default
    private boolean ativa = false;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() {
        this.criadoEm = LocalDateTime.now();
    }
}