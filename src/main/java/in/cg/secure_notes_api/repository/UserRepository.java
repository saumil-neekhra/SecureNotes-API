package in.cg.secure_notes_api.repository;

import in.cg.secure_notes_api.entity.ProviderType;
import in.cg.secure_notes_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
