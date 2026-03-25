package com.sejus.sigeplan.application.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRoleRequest(

        @NotBlank(message = "Nome do papel é obrigatório.")
        @Size(min = 3, max = 80, message = "Nome do papel deve ter entre 3 e 80 caracteres.")
        String name,

        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres.")
        String description
) {
}