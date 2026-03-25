package com.sejus.sigeplan.application.service;

import com.sejus.sigeplan.application.dto.AuthResponse;
import com.sejus.sigeplan.application.dto.AuthenticatedUserResponse;
import com.sejus.sigeplan.application.dto.LoginRequest;
import com.sejus.sigeplan.application.dto.RegisterUserRequest;
import com.sejus.sigeplan.domain.model.Role;
import com.sejus.sigeplan.domain.model.User;
import com.sejus.sigeplan.domain.repository.RoleRepository;
import com.sejus.sigeplan.domain.repository.UserRepository;
import com.sejus.sigeplan.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String DEFAULT_ROLE_NAME = "ROLE_USER";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse register(RegisterUserRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        String normalizedCpf = normalizeCpf(request.cpf());

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }

        if (userRepository.existsByCpf(normalizedCpf)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado.");
        }

        Role defaultRole = roleRepository.findByName(DEFAULT_ROLE_NAME)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Role padrão não encontrada."
                ));

        OffsetDateTime now = OffsetDateTime.now();

        User user = new User(
                UUID.randomUUID(),
                request.fullName().trim(),
                normalizedEmail,
                normalizedCpf,
                passwordEncoder.encode(request.password()),
                true,
                Set.of(defaultRole),
                Set.of(),
                now,
                now
        );

        User savedUser = userRepository.save(user);
        return buildAuthResponse(savedUser);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String normalizedCpf = normalizeCpf(request.cpf());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedCpf, request.password())
        );

        User user = userRepository.findByCpf(normalizedCpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas."));

        if (!user.active()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário inativo.");
        }

        return buildAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public AuthenticatedUserResponse me(String cpf) {
        User user = userRepository.findByCpf(normalizeCpf(cpf))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));

        return new AuthenticatedUserResponse(
                user.id().toString(),
                user.fullName(),
                user.email(),
                user.cpf(),
                user.roles().stream().map(Role::name).collect(Collectors.toSet())
        );
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user);

        return new AuthResponse(
                token,
                "Bearer",
                jwtService.getExpirationInSeconds(),
                new AuthResponse.UserResponse(
                        user.id().toString(),
                        user.fullName(),
                        user.email(),
                        user.cpf(),
                        user.roles().stream().map(Role::name).collect(Collectors.toSet())
                )
        );
    }

    private String normalizeCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }
}