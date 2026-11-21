package com.artur.alexa_skill_backend.comando.controller;

import com.artur.alexa_skill_backend.comando.dto.RegraComandoRequest;
import com.artur.alexa_skill_backend.comando.dto.RegraComandoResponse;
import com.artur.alexa_skill_backend.comando.service.RegraComandoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandos")
@RequiredArgsConstructor
public class RegraComandoController {

    private final RegraComandoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegraComandoResponse criar(@RequestBody @Valid RegraComandoRequest request) {
        return service.criar(request);
    }

    @GetMapping
    public List<RegraComandoResponse> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}")
    public RegraComandoResponse atualizar(@PathVariable Long id, @RequestBody @Valid RegraComandoRequest request) {
        return service.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}