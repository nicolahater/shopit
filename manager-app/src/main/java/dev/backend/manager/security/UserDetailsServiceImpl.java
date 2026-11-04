package dev.backend.manager.security;

import dev.backend.manager.entity.Authority;
import dev.backend.manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // without it we will be thrown exception because of Lazy fetching of ManyToMany relation on AppUser.authorities field. TODO: check if it throws an exception if we comment this line
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("encoded: " + new BCryptPasswordEncoder().encode("password"));
        return userRepository.findByUsername(username)
                .map(user -> User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
//                        .authorities(user.getAuthorities().stream().map(Authority::getAuthority).toArray(String[]::new))
                        .authorities(user.getAuthorities().stream()
                                .map(Authority::getAuthority)
                                .map(SimpleGrantedAuthority::new)
                                .toList())
//                                .toArray(SimpleGrantedAuthority[]::new))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User '%s' not found".formatted(username)));
    }
}
