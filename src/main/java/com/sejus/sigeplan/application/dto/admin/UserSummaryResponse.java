package com.sejus.sigeplan.application.dto.admin;

import java.util.Set;

public record UserSummaryResponse(
        String id,
        String fullName,
        String email,
        String cpf,
        boolean active,
        Set<String> roles,
        Set<String> units
) {
}