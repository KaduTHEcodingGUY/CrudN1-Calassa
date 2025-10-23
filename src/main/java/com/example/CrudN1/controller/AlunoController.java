package com.example.CrudN1.controller;

import com.example.CrudN1.model.Aluno;
import com.example.CrudN1.repository.AlunoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    @GetMapping
    public List<Aluno> list() {
        return alunoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> get(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoRepository.findById(id);
        return aluno.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Aluno body) {
        if (body.getId() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID must be null when creating");
        }
        if (body.getNome() == null || body.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nome and email are required");
        }
        if (alunoRepository.existsByEmail(body.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("email already exists");
        }
        Aluno saved = alunoRepository.save(body);
        return ResponseEntity.created(URI.create("/api/alunos/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Aluno body) {
        Optional<Aluno> existing = alunoRepository.findById(id);
        if (existing.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Aluno aluno = existing.get();
        if (body.getNome() != null) aluno.setNome(body.getNome());
        if (body.getEmail() != null) aluno.setEmail(body.getEmail());
        if (body.getMatricula() != null) aluno.setMatricula(body.getMatricula());
        if (body.getTelefone() != null) aluno.setTelefone(body.getTelefone());
        if (body.getEscola() != null) aluno.setEscola(body.getEscola());
        if (body.getTurma() != null) aluno.setTurma(body.getTurma());
        if (body.getTurno() != null) aluno.setTurno(body.getTurno());
        Aluno saved = alunoRepository.save(aluno);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!alunoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alunoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
