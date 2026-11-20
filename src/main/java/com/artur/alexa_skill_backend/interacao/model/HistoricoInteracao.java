package com.artur.alexa_skill_backend.interacao.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historico_interacoes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HistoricoInteracao {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "intent_recebida", nullable = false, length = 100)
        private String intentRecebida;

        @Column(name = "texto_usuario", columnDefinition = "TEXT")
        private String textoUsuario;

        @Column(name = "resposta_enviada", nullable = false, columnDefinition = "TEXT")
        private String respostaEnviada;

        @Column(name = "voz_utilizada", nullable = false, length = 30)
        private String vozUtilizada;

        @Column(name = "data_hora", nullable = false, updatable = false)
        private LocalDateTime dataHora;

        @PrePersist
        void prePersist() {
            this.dataHora = LocalDateTime.now();
        }
}
