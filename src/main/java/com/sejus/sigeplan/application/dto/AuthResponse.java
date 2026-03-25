package com.sejus.sigeplan.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(name = "AuthResponse", description = "Resposta de autenticação")
public record AuthResponse(

        @Schema(description = "Token JWT de acesso", example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(description = "Tipo do token", example = "Bearer")
        String tokenType,

        @Schema(description = "Tempo de expiração do token em segundos", example = "86400")
        long expiresIn,

        UserResponse user
) {
    public record UserResponse(
            String id,
            String fullName,
            String email,
            String cpf,
            Set<String> roles
    ) {
    }
}