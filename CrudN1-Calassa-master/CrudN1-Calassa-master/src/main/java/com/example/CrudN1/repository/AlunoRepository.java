package com.example.CrudN1.repository;

import com.example.CrudN1.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    boolean existsByEmail(String email);
    boolean existsByMatricula(String matricula);
    List<Aluno> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(String nome, String email);
}



