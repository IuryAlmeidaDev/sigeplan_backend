package com.sejus.sigeplan.domain.repository;

import com.sejus.sigeplan.domain.model.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {

    Optional<Role> findById(UUID id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    boolean existsByName(String name);

    Role save(Role role);
}