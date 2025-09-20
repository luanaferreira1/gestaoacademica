package com.example.gestaoacademica.repository;

import com.example.gestaoacademica.model.Aluno;
import com.example.gestaoacademica.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Optional<Matricula> findByAlunoIdAndDisciplinaId(Long alunoId, Long disciplinaId);

    @Query("SELECT m.aluno FROM Matricula m WHERE m.disciplina.id = :disciplinaId AND m.nota >= 7.0")
    List<Aluno> findAlunosAprovados(Long disciplinaId);

    @Query("SELECT m.aluno FROM Matricula m WHERE m.disciplina.id = :disciplinaId AND m.nota < 7.0")
    List<Aluno> findAlunosReprovados(Long disciplinaId);
}