package com.sejus.sigeplan.application.service;

import com.sejus.sigeplan.application.dto.admin.UpdateUserRequest;
import com.sejus.sigeplan.application.dto.admin.UpdateUserStatusRequest;
import com.sejus.sigeplan.application.dto.admin.UserSummaryResponse;
import com.sejus.sigeplan.domain.model.Role;
import com.sejus.sigeplan.domain.model.User;
import com.sejus.sigeplan.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserSummaryResponse> findAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserSummaryResponse findById(UUID userId) {
        User user = getUserOrThrow(userId);
        return toResponse(user);
    }

    @Transactional
    public UserSummaryResponse update(UUID userId, UpdateUserRequest request) {
        User currentUser = getUserOrThrow(userId);

        String normalizedEmail = request.email().trim().toLowerCase();
        String normalizedCpf = normalizeCpf(request.cpf());

        userRepository.findByEmail(normalizedEmail)
                .filter(found -> !found.id().equals(userId))
                .ifPresent(found -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
                });

        userRepository.findByCpf(normalizedCpf)
                .filter(found -> !found.id().equals(userId))
                .ifPresent(found -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "CPF já cadastrado.");
                });

        User updatedUser = new User(
                currentUser.id(),
                request.fullName().trim(),
                normalizedEmail,
                normalizedCpf,
                currentUser.passwordHash(),
                currentUser.active(),
                currentUser.roles(),
                currentUser.units(),
                currentUser.createdAt(),
                OffsetDateTime.now()
        );

        return toResponse(userRepository.save(updatedUser));
    }

    @Transactional
    public UserSummaryResponse assignRole(UUID userId, Role role) {
        User currentUser = getUserOrThrow(userId);

        Set<Role> updatedRoles = new HashSet<>(currentUser.roles());
        updatedRoles.add(role);

        User updatedUser = new User(
                currentUser.id(),
                currentUser.fullName(),
                currentUser.email(),
                currentUser.cpf(),
                currentUser.passwordHash(),
                currentUser.active(),
                updatedRoles,
                currentUser.units(),
                currentUser.createdAt(),
                OffsetDateTime.now()
        );

        return toResponse(userRepository.save(updatedUser));
    }

    @Transactional
    public UserSummaryResponse removeRole(UUID userId, UUID roleId) {
        User currentUser = getUserOrThrow(userId);

        Set<Role> updatedRoles = currentUser.roles().stream()
                .filter(role -> !role.id().equals(roleId))
                .collect(Collectors.toSet());

        User updatedUser = new User(
                currentUser.id(),
                currentUser.fullName(),
                currentUser.email(),
                currentUser.cpf(),
                currentUser.passwordHash(),
                currentUser.active(),
                updatedRoles,
                currentUser.units(),
                currentUser.createdAt(),
                OffsetDateTime.now()
        );

        return toResponse(userRepository.save(updatedUser));
    }

    @Transactional
    public UserSummaryResponse updateStatus(UUID userId, UpdateUserStatusRequest request) {
        User currentUser = getUserOrThrow(userId);

        User updatedUser = new User(
                currentUser.id(),
                currentUser.fullName(),
                currentUser.email(),
                currentUser.cpf(),
                currentUser.passwordHash(),
                request.active(),
                currentUser.roles(),
                currentUser.units(),
                currentUser.createdAt(),
                OffsetDateTime.now()
        );

        return toResponse(userRepository.save(updatedUser));
    }

    private User getUserOrThrow(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado."));
    }

    private UserSummaryResponse toResponse(User user) {
        return new UserSummaryResponse(
                user.id().toString(),
                user.fullName(),
                user.email(),
                user.cpf(),
                user.active(),
                user.roles().stream().map(Role::name).collect(Collectors.toSet()),
                user.units().stream().map(unit -> unit.name()).collect(Collectors.toSet())
        );
    }

    private String normalizeCpf(String cpf) {
        return cpf == null ? null : cpf.replaceAll("\\D", "");
    }
}