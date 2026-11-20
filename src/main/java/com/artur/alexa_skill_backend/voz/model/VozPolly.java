package com.artur.alexa_skill_backend.voz.model;

import lombok.Getter;

@Getter
public enum VozPolly {
    CAMILA("Camila"),
    VITORIA("Vitoria"),
    RICARDO("Ricardo"),
    THIAGO("Thiago");

    private final String nomeSslm;

    VozPolly(String nomeSslm){
        this.nomeSslm = nomeSslm;
    }
}
