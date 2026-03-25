package com.sejus.sigeplan.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(name = "LoginRequest", description = "Dados para autenticação do usuário")
public record LoginRequest(

        @Schema(description = "CPF do usuário", example = "12345678901")
        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos.")
        String cpf,

        @Schema(description = "Senha do usuário", example = "123456")
        @NotBlank(message = "Senha é obrigatória.")
        String password
) {
}