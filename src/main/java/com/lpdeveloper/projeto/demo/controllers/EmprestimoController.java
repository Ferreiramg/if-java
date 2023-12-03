package com.lpdeveloper.projeto.demo.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.lpdeveloper.projeto.demo.models.Emprestimo;
import com.lpdeveloper.projeto.demo.repositories.EmprestimoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/emprestimos")
@Tag(name = "Emprestimos")
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;

    @Autowired
    public EmprestimoController(EmprestimoRepository emprestimoRepository) {
        this.emprestimoRepository = emprestimoRepository;
    }

    @GetMapping
    @Operation(summary ="Obter todos os Empréstimos")
    public List<Emprestimo> getAllEmprestimos() {
        return emprestimoRepository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary ="Obter Empréstimo por ID")
    public Emprestimo getEmprestimoById(@PathVariable Long id) {
        return emprestimoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprestimo não encontrado"));
    }

    @PostMapping
    @Operation(summary ="Criar um novo Empréstimo")
    public Emprestimo createEmprestimo(@RequestBody Emprestimo emprestimo) {
        return emprestimoRepository.save(emprestimo);
    }

    @PutMapping("/{id}")
    @Operation(summary ="Atualizar um Empréstimo existente")
    public Emprestimo updateEmprestimo(@PathVariable Long id, @RequestBody Emprestimo emprestimo) {
        if (!emprestimoRepository.existsById(id)) {
            throw new RuntimeException("Emprestimo não encontrado");
        }
        emprestimo.setId(id);
        return emprestimoRepository.save(emprestimo);
    }

    @DeleteMapping("/{id}")
    @Operation(summary ="Excluir um Empréstimo por ID")
    public void deleteEmprestimo(@PathVariable Long id) {
        emprestimoRepository.deleteById(id);
    }
}

