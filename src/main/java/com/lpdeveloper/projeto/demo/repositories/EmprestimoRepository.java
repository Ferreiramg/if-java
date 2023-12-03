package com.lpdeveloper.projeto.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lpdeveloper.projeto.demo.models.Emprestimo;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Consulta para encontrar empréstimos por contrato
    List<Emprestimo> findByContract(String contract);

    List<Emprestimo> findBySolicitacaoId(Long id);

    // Adicione mais métodos de consulta conforme necessário
}

