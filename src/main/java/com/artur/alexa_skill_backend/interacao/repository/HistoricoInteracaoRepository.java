package com.artur.alexa_skill_backend.interacao.repository;

import com.artur.alexa_skill_backend.interacao.model.HistoricoInteracao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoInteracaoRepository extends JpaRepository <HistoricoInteracao, Long> {

    List<HistoricoInteracao> findTop50ByOrderByDataHoraDesc();
}
