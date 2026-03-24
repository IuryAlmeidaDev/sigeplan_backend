import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "RegisterUserRequest", description = "Dados para cadastro de usuário")
public record RegisterUserRequest(
        @Schema(example = "Iury Costa")
        @NotBlank @Size(min = 3, max = 120) String fullName,

        @Schema(example = "iury@sigeplan.gov.br")
        @NotBlank @Email String email,

        @Schema(example = "123456")
        @NotBlank @Size(min = 6, max = 100) String password
) {
}