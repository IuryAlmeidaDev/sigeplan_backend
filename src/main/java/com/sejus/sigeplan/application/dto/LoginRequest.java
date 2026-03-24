import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "LoginRequest", description = "Dados para autenticação do usuário")
public record LoginRequest(
        @Schema(example = "admin@sigeplan.gov.br")
        @NotBlank @Email String email,

        @Schema(example = "123456")
        @NotBlank String password
) {
}