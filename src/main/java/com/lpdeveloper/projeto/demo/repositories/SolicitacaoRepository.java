package com.lpdeveloper.projeto.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.lpdeveloper.projeto.demo.models.Solicitacao;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long>, JpaSpecificationExecutor<Solicitacao> {

    List<Solicitacao> findAllByUserId(Long id);

    List<Solicitacao> findAll(@Nullable Specification<Solicitacao> spec);
}
