package dev.backend.manager.repository;

import dev.backend.manager.entity.AppUser;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;

public interface UserRepository extends ListCrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}
