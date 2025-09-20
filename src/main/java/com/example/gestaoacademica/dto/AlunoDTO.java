package com.example.gestaoacademica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AlunoDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;
    @Email(message = "Formato de e-mail inválido")
    private String email;
    private String telefone;
    private String endereco;
}