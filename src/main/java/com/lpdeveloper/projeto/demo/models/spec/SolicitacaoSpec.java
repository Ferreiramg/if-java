package com.lpdeveloper.projeto.demo.models.spec;

import org.springframework.data.jpa.domain.Specification;

import com.lpdeveloper.projeto.demo.models.Solicitacao;

public class SolicitacaoSpec {
    public static Specification<Solicitacao> actives() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("deletedAt"));
    }

}
