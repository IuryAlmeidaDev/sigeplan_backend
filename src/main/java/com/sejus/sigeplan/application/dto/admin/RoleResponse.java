package com.sejus.sigeplan.application.dto.admin;

import java.util.Set;

public record RoleResponse(
        String id,
        String name,
        String description,
        Set<String> permissions
) {
}