package com.artur.alexa_skill_backend.voz.repository;

import com.artur.alexa_skill_backend.voz.model.ConfiguracaoVoz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConfiguracaoVozRepository extends JpaRepository<ConfiguracaoVoz,Long> {

    Optional<ConfiguracaoVoz> findByAtivaTrue();

    boolean existsByNomePerfilIgnoreCase(String nomePerfil);

    @Modifying
    @Query("UPDATE ConfiguracaoVoz v SET v.ativa = false WHERE v.ativa = true")
    void desativarTodas();
}

