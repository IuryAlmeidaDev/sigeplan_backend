package com.sejus.sigeplan.interfaces.rest;

import com.sejus.sigeplan.application.dto.admin.UpdateUserRequest;
import com.sejus.sigeplan.application.dto.admin.UpdateUserStatusRequest;
import com.sejus.sigeplan.application.dto.admin.UserSummaryResponse;
import com.sejus.sigeplan.application.service.UserAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserAdminController {

    private final UserAdminService userAdminService;

    @GetMapping
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Listar usuários")
    public List<UserSummaryResponse> findAll() {
        return userAdminService.findAll();
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Buscar usuário por ID")
    public UserSummaryResponse findById(@PathVariable UUID userId) {
        return userAdminService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @Operation(summary = "Atualizar usuário")
    public UserSummaryResponse update(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return userAdminService.update(userId, request);
    }

    @PatchMapping("/{userId}/status")
    @PreAuthorize("hasAuthority('USER_DISABLE')")
    @Operation(summary = "Ativar ou desativar usuário")
    public UserSummaryResponse updateStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest request
    ) {
        return userAdminService.updateStatus(userId, request);
    }
}