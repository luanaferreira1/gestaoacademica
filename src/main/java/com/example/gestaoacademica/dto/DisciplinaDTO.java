package com.example.gestaoacademica.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DisciplinaDTO {
    @NotBlank(message = "O nome da disciplina é obrigatório")
    private String nome;
    @NotBlank(message = "O código da disciplina é obrigatório")
    private String codigo;
}