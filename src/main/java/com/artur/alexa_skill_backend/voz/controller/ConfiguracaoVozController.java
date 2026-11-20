package com.artur.alexa_skill_backend.voz.controller;

import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozRequest;
import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozResponse;
import com.artur.alexa_skill_backend.voz.service.ConfiguracaoVozService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vozes")
@RequiredArgsConstructor
public class ConfiguracaoVozController {

    private final ConfiguracaoVozService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConfiguracaoVozResponse criar(@RequestBody @Valid ConfiguracaoVozRequest request) {
        return service.postVoz(request);
    }

    @GetMapping
    public List<ConfiguracaoVozResponse> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/ativa")
    public ConfiguracaoVozResponse buscarAtiva() {
        return service.buscarAtiva();
    }

    @PatchMapping("/{id}/ativar")
    public ConfiguracaoVozResponse ativar(@PathVariable Long id) {
        return service.ativar(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}