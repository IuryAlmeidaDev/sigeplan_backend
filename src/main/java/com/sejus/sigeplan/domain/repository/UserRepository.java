package com.sejus.sigeplan.domain.repository;

import com.sejus.sigeplan.domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByCpf(String cpf);

    Optional<User> findById(UUID id);

    List<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    User save(User user);
}