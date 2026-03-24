import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

@Schema(name = "AuthResponse", description = "Resposta de autenticação")
public record AuthResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9...")
        String accessToken,

        @Schema(example = "Bearer")
        String tokenType,

        @Schema(example = "86400000")
        long expiresIn,

        UserResponse user
) {
    @Schema(name = "AuthUserResponse", description = "Dados do usuário autenticado")
    public record UserResponse(
            @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
            String id,

            @Schema(example = "Iury Costa")
            String fullName,

            @Schema(example = "iury@sigeplan.gov.br")
            String email,

            Set<String> roles
    ) {
    }
}