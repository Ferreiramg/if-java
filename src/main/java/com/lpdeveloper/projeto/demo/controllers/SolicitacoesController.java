package com.lpdeveloper.projeto.demo.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lpdeveloper.projeto.demo.dto.SolicitacaoDTO;
import com.lpdeveloper.projeto.demo.models.Emprestimo;
import com.lpdeveloper.projeto.demo.models.Solicitacao;
import com.lpdeveloper.projeto.demo.models.spec.SolicitacaoSpec;
import com.lpdeveloper.projeto.demo.repositories.EmprestimoRepository;
import com.lpdeveloper.projeto.demo.repositories.SolicitacaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("solicitacao")
@Tag(name = "Solicitação Emprestimos")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class SolicitacoesController {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Operation(summary = "Cria uma nova solicitação de empréstimo")
    @PostMapping
    public Solicitacao create(@RequestBody SolicitacaoDTO s) {

        var authUser = AuthController.getAuthenticatedUser();
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setAmount(s.amount);
        solicitacao.setValue(s.value);
        solicitacao.setUser(authUser);

        return solicitacaoRepository.save(solicitacao);
    }

    @GetMapping("/user")
    @Operation(summary = "Lista todas as solicitações de empréstimo do usuário autenticado")
    public List<Solicitacao> listByUser() {

        var authUser = AuthController.getAuthenticatedUser();

        Specification<Solicitacao> specuser = Specification.where(SolicitacaoSpec.actives())
                .and((r, q, c) -> c.equal(r.get("user"), authUser.getId()));

        return solicitacaoRepository.findAll(specuser);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma solicitação de empréstimo pelo ID")
    public Solicitacao getSolicitacaoById(
            @PathVariable @Parameter(description = "ID da solicitação de empréstimo") Long id) {

        var solicitacao = solicitacaoRepository.findById(id).orElse(null);

        if (solicitacao == null) {
            return null;
        }

        return solicitacao;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma solicitação de empréstimo pelo ID")
    public ResponseEntity<Solicitacao> update(@RequestBody SolicitacaoDTO s) {

        Solicitacao solicitacao = solicitacaoRepository.findById(s.id).orElseThrow(null);

        solicitacao.setValue(s.value);
        solicitacao.setAmount(s.amount);

        final Solicitacao updatedSolicitacao = solicitacaoRepository.save(solicitacao);
        return ResponseEntity.ok(updatedSolicitacao);
    }

    @Operation(summary = "Exclui uma solicitação de empréstimo pelo ID")
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID da solicitação de empréstimo", required = true) @PathVariable Long id) {
        solicitacaoRepository.deleteById(id);
    }

    @Operation(summary = "Lista todas as solicitações de empréstimo. (Somente administradores)")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<Solicitacao> getAll() {
        return solicitacaoRepository.findAll();
    }

    @Operation(summary = "Cria os empréstimos correspondentes e atualiza a solicitação. (Somente administradores)")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @PutMapping("/admin/accept/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Solicitacao> accept(
            @Parameter(description = "ID da solicitação de empréstimo", required = true) @PathVariable Long id) {

        Solicitacao solicitacao = solicitacaoRepository.findById(id).orElseThrow(null);

        if (solicitacao == null) {
            return ResponseEntity.notFound().build();
        }

        solicitacao.setAprovedAt(new Date());

        var amount = solicitacao.getAmount();

        var date = new Date();

        for (int i = 0; i < amount; i++) {
            var emprestimo = new Emprestimo();
            emprestimo.setSolicitacao(solicitacao);
            emprestimo.setAmount(i + 1);
            emprestimo.setContract("Contrato #" + solicitacao.getId());
            emprestimo.setStartAt(date);
            emprestimo.setTax(0.05);
            emprestimo.setValue(solicitacao.getValue() / amount);

            emprestimoRepository.save(emprestimo);

        }

        solicitacaoRepository.save(solicitacao);

        return ResponseEntity.ok(solicitacao);
    }

}
