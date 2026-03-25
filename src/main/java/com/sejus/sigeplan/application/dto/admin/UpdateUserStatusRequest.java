package com.sejus.sigeplan.application.dto.admin;

import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "Status é obrigatório.")
        Boolean active
) {
}