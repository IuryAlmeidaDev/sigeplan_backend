package com.sejus.sigeplan.infrastructure.security;

import com.sejus.sigeplan.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByCpf(username)
                .map(user -> new AuthenticatedUser(
                        user.id().toString(),
                        user.cpf(),
                        user.email(),
                        user.fullName(),
                        user.passwordHash(),
                        user.active(),
                        user.roles()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
}