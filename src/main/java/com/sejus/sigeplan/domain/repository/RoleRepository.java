package com.sejus.sigeplan.domain.repository;

import com.sejus.sigeplan.domain.model.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(String name);
}