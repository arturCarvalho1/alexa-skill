package com.artur.alexa_skill_backend.comando.service;

import com.artur.alexa_skill_backend.comando.dto.RegraComandoRequest;
import com.artur.alexa_skill_backend.comando.dto.RegraComandoResponse;
import com.artur.alexa_skill_backend.comando.model.RegraComando;
import com.artur.alexa_skill_backend.comando.repository.RegraComandoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RegraComandoService {

    private static final String FALLBACK_PADRAO =
            "Foi mal cachorro, O burro do meu desenvolvedor ainda não configurou uma resposta pra esse comando. ";

    private final RegraComandoRepository repository;

    @Transactional
    public RegraComandoResponse criar(RegraComandoRequest request){
        if (repository.existsByIntentNameIgnoreCase(request.intentName().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe uma regra para essa intent");
        }

        RegraComando regra = RegraComando.builder()
                .intentName(request.intentName().trim())
                .gatilho(request.gatilho().trim())
                .templateResposta(request.templateResposta().trim())
                .ativo(request.ativo() == null || request.ativo())
                .build();

        return RegraComandoResponse.fromEntity(repository.save(regra));
    }

    @Transactional(readOnly = true)
    public List<RegraComandoResponse> listarTodos() {
        return repository.findAll().stream()
                .map(RegraComandoResponse::fromEntity)
                .toList();
    }

    @Transactional
    public RegraComandoResponse atualizar(Long id, RegraComandoRequest request) {
        RegraComando regra = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Regra de comando não encontrada"));

        regra.setIntentName(request.intentName().trim());
        regra.setGatilho(request.gatilho().trim());
        regra.setTemplateResposta(request.templateResposta().trim());
        if (request.ativo() != null) {
            regra.setAtivo(request.ativo());
        }

        return RegraComandoResponse.fromEntity(repository.save(regra));
    }

    @Transactional
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Regra de comando não encontrada");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public String buscarRespostaParaIntent(String intentName) {
        return repository.findByIntentNameIgnoreCaseAndAtivoTrue(intentName)
                .map(RegraComando::getTemplateResposta)
                .orElse(FALLBACK_PADRAO);
    }
}