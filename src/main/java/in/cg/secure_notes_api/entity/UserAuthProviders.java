package in.cg.secure_notes_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserAuthProviders {

//    For Storing OAuthLogin details so no confusion occurs;
//    Any user can log in with Google, email, GitHub, and will still contain same userId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private Long userId;

    private String email;

    private String providerId;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

}
