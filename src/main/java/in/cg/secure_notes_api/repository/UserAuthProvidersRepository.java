package in.cg.secure_notes_api.repository;

import in.cg.secure_notes_api.entity.ProviderType;
import in.cg.secure_notes_api.entity.UserAuthProviders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAuthProvidersRepository extends JpaRepository<UserAuthProviders,String> {

    boolean existsByEmailAndProviderIdAndProviderType(String email, String providerId, ProviderType providerType);
}
