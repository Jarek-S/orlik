package pl.ipolice.orlik.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ipolice.orlik.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKeycloakId(String keycloakId);
}
