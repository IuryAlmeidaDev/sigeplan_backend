package com.sejus.sigeplan.application.dto.admin;

import jakarta.validation.constraints.NotNull;

public record UpdateUnitStatusRequest(
        @NotNull(message = "Status é obrigatório.")
        Boolean active
) {}