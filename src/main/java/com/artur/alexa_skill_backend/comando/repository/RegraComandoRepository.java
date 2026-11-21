package com.artur.alexa_skill_backend.comando.repository;

import com.artur.alexa_skill_backend.comando.model.RegraComando;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegraComandoRepository extends JpaRepository<RegraComando,Long> {

    Optional<RegraComando> findByIntentNameIgnoreCaseAndAtivoTrue(String intentName);

    boolean existsByIntentNameIgnoreCase(String intentName);
}
