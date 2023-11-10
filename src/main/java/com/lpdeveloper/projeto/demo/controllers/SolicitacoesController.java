package com.lpdeveloper.projeto.demo.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpdeveloper.projeto.demo.models.Solicitacao;
import com.lpdeveloper.projeto.demo.models.User;
import com.lpdeveloper.projeto.demo.models.spec.SolicitacaoSpec;
import com.lpdeveloper.projeto.demo.repositories.SolicitacaoRepository;

@RestController
@RequestMapping("/emprestimos")

public class SolicitacoesController {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @PostMapping("/solicitacao")
    public Solicitacao create(@RequestBody Solicitacao solicitacao, @AuthenticationPrincipal User user) {

        solicitacao.setUser(user);

        return solicitacaoRepository.save(solicitacao);
    }

    @PutMapping("/solicitacao/{id}")
    public ResponseEntity<Solicitacao> update(@RequestBody Solicitacao solicitacaoDetails) {

        Solicitacao solicitacao = solicitacaoRepository.findById(solicitacaoDetails.getId()).orElseThrow(null);

        solicitacao.setValue(solicitacaoDetails.getValue());
        solicitacao.setAmount(solicitacaoDetails.getAmount());

        final Solicitacao updatedSolicitacao = solicitacaoRepository.save(solicitacao);
        return ResponseEntity.ok(updatedSolicitacao);
    }

    @PutMapping("/solicitacao/accept/{id}")
    public ResponseEntity<Solicitacao> accept(@PathVariable(value = "id") Long id) {

        Solicitacao solicitacao = solicitacaoRepository.findById(id).orElseThrow(null);

        solicitacao.setAprovedAt(new Date());

        final Solicitacao updatedSolicitacao = solicitacaoRepository.save(solicitacao);

        return ResponseEntity.ok(updatedSolicitacao);
    }

    @GetMapping("/solicitacoes")
    public List<Solicitacao> listByUser(@AuthenticationPrincipal User user) {

        Specification<Solicitacao> specuser = Specification.where(SolicitacaoSpec.actives())
                .and((r, q, c) -> c.equal(r.get("user"), user.getId()));

        return solicitacaoRepository.findAll(specuser);
    }

    // Endpoint para deletar um usuário
    @DeleteMapping("/solicitacao/{id}")
    public void deleteUser(@PathVariable Long id) {
        solicitacaoRepository.deleteById(id);
    }

    @GetMapping("/admin/solicitacoes")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Solicitacao> getAll() {
        return solicitacaoRepository.findAll();
    }

}
