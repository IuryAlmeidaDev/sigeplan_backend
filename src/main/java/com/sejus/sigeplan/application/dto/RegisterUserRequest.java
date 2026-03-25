package com.sejus.sigeplan.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterUserRequest", description = "Dados para cadastro de usuário")
public record RegisterUserRequest(

        @Schema(description = "Nome completo do usuário", example = "Iury Costa")
        @NotBlank(message = "Nome completo é obrigatório.")
        @Size(min = 3, max = 120, message = "Nome completo deve ter entre 3 e 120 caracteres.")
        String fullName,

        @Schema(description = "CPF do usuário", example = "12345678901")
        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos.")
        String cpf,

        @Schema(description = "E-mail do usuário", example = "iury@sigeplan.gov.br")
        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @Schema(description = "Senha do usuário", example = "123456")
        @NotBlank(message = "Senha é obrigatória.")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres.")
        String password
) {
}