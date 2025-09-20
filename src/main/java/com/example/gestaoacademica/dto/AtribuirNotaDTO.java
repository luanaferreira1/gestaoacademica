package com.example.gestaoacademica.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AtribuirNotaDTO {
    @NotNull(message = "O ID do aluno é obrigatório")
    private Long alunoId;
    @NotNull(message = "O ID da disciplina é obrigatório")
    private Long disciplinaId;
    @NotNull(message = "A nota é obrigatória")
    @Min(value = 0, message = "A nota não pode ser menor que 0")
    @Max(value = 10, message = "A nota não pode ser maior que 10")
    private Double nota;
}