package com.sejus.sigeplan.application.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

        @NotBlank(message = "Nome completo é obrigatório.")
        @Size(min = 3, max = 120, message = "Nome completo deve ter entre 3 e 120 caracteres.")
        String fullName,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail inválido.")
        String email,

        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos.")
        String cpf
) {
}