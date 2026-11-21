package com.artur.alexa_skill_backend.voz.service;

import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozRequest;
import com.artur.alexa_skill_backend.voz.dto.ConfiguracaoVozResponse;
import com.artur.alexa_skill_backend.voz.model.ConfiguracaoVoz;
import com.artur.alexa_skill_backend.voz.repository.ConfiguracaoVozRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfiguracaoVozService {

    private final ConfiguracaoVozRepository repository;

    @Transactional
    public ConfiguracaoVozResponse postVoz(ConfiguracaoVozRequest request){
        if (repository.existsByNomePerfilIgnoreCase(request.nomePerfil())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um perfil com esse nome");
        }

    boolean deveAtivar = Boolean.TRUE.equals(request.ativarAgora()) || repository.count() == 0;
    if (deveAtivar){
            repository.desativarTodas();
        }
        ConfiguracaoVoz novaVoz = ConfiguracaoVoz.builder()
                .nomePerfil(request.nomePerfil().trim())
                .vozPolly(request.vozPolly())
                .velocidade(request.velocidade() !=null && !request.velocidade().isBlank() ? request.velocidade() : "medium")
                .tom(request.tom() !=null && !request.tom().isBlank() ? request.tom() : "medium")
                .ativa(deveAtivar)
                .build();

        return ConfiguracaoVozResponse.fromEntity(repository.save(novaVoz));
    }

    @Transactional(readOnly = true)
    public List<ConfiguracaoVozResponse> listarTodas() {
        return repository.findAll().stream()
                .map(ConfiguracaoVozResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ConfiguracaoVozResponse buscarAtiva() {
        ConfiguracaoVoz ativa = repository.findByAtivaTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nenhuma voz ativa configurada"));
        return ConfiguracaoVozResponse.fromEntity(ativa);
    }

    @Transactional
    public ConfiguracaoVozResponse ativar(Long id) {
        ConfiguracaoVoz voz = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de voz não encontrado"));

        repository.desativarTodas();
        voz.setAtiva(true);

        return ConfiguracaoVozResponse.fromEntity(repository.save(voz));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil de voz não encontrado");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ConfiguracaoVozResponse> buscarAtivaOpcional() {
        return repository.findByAtivaTrue()
                .map(ConfiguracaoVozResponse::fromEntity);
    }
}