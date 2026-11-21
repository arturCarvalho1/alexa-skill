package com.artur.alexa_skill_backend.interacao.service;

import com.artur.alexa_skill_backend.interacao.model.HistoricoInteracao;
import com.artur.alexa_skill_backend.interacao.repository.HistoricoInteracaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoInteracaoService {

    private final HistoricoInteracaoRepository repository;

    @Transactional
    public void registrar(String intentRecebida, String textoUsuario, String respostaEnviada, String vozUtilizada) {
        HistoricoInteracao log = HistoricoInteracao.builder()
                .intentRecebida(intentRecebida)
                .textoUsuario(textoUsuario)
                .respostaEnviada(respostaEnviada)
                .vozUtilizada(vozUtilizada)
                .build();

        repository.save(log);
    }

    @Transactional(readOnly = true)
    public List<HistoricoInteracao> listarRecentes() {
        return repository.findTop50ByOrderByDataHoraDesc();
    }
}