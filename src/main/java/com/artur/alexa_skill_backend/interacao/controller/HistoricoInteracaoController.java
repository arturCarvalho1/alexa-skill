package com.artur.alexa_skill_backend.interacao.controller;

import com.artur.alexa_skill_backend.interacao.model.HistoricoInteracao;
import com.artur.alexa_skill_backend.interacao.service.HistoricoInteracaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/interacoes")
@RequiredArgsConstructor
public class HistoricoInteracaoController {

    private final HistoricoInteracaoService service;

    @GetMapping
    public List<HistoricoInteracao> listarRecentes() {
        return service.listarRecentes();
    }
}
