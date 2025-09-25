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

    private final AlunoRepository repo;

    public AlunoController(AlunoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Aluno> list(@RequestParam(value = "q", required = false) String query) {
        if (query == null || query.isBlank()) {
            return repo.findAll();
        }
        return repo.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluno> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Aluno body) {
        if (body.getId() != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID must be null when creating");
        if (body.getNome() == null || body.getEmail() == null || body.getMatricula() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nome, email e matricula são obrigatórios");
        }
        if (repo.existsByEmail(body.getEmail())) return ResponseEntity.status(HttpStatus.CONFLICT).body("email já existe");
        if (repo.existsByMatricula(body.getMatricula())) return ResponseEntity.status(HttpStatus.CONFLICT).body("matricula já existe");
        Aluno saved = repo.save(body);
        return ResponseEntity.created(URI.create("/api/alunos/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Aluno body) {
        Optional<Aluno> existing = repo.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();
        Aluno a = existing.get();
        if (body.getNome() != null) a.setNome(body.getNome());
        if (body.getEmail() != null) a.setEmail(body.getEmail());
        if (body.getMatricula() != null) a.setMatricula(body.getMatricula());
        if (body.getTelefone() != null) a.setTelefone(body.getTelefone());
        if (body.getEscola() != null) a.setEscola(body.getEscola());
        if (body.getTurma() != null) a.setTurma(body.getTurma());
        if (body.getTurno() != null) a.setTurno(body.getTurno());
        Aluno saved = repo.save(a);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


