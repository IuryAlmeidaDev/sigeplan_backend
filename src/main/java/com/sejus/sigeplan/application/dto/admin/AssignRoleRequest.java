package com.sejus.sigeplan.application.dto.admin;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignRoleRequest(
        @NotNull(message = "roleId é obrigatório.")
        UUID roleId
) {
}