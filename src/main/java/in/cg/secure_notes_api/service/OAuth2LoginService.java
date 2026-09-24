package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.dto.LoginResponseDTO;
import in.cg.secure_notes_api.entity.ProviderType;
import in.cg.secure_notes_api.entity.Role;
import in.cg.secure_notes_api.entity.User;
import in.cg.secure_notes_api.entity.UserAuthProviders;
import in.cg.secure_notes_api.repository.UserAuthProvidersRepository;
import in.cg.secure_notes_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class OAuth2LoginService {
    private final OAuth2ExtrationUtil oAuth2ExtrationUtil;
    private final UserRepository userRepository;
    private final UserAuthProvidersRepository userAuthProvidersRepository;
    private final JwtUtilityService jwtUtilityService;

    public OAuth2LoginService(OAuth2ExtrationUtil oAuth2ExtrationUtil, UserRepository userRepository, UserAuthProvidersRepository userAuthProvidersRepository, JwtUtilityService jwtUtilityService) {
        this.oAuth2ExtrationUtil = oAuth2ExtrationUtil;
        this.userRepository = userRepository;
        this.userAuthProvidersRepository = userAuthProvidersRepository;
        this.jwtUtilityService = jwtUtilityService;
    }

    @Transactional
    public LoginResponseDTO handleOAuth2LoginRequest(OAuth2User oAuth2User, String registrationId) {
        ProviderType providerType = oAuth2ExtrationUtil.getAuthProviderTypeFromRegistrationId(registrationId);

        String providerId = oAuth2ExtrationUtil.determineProviderIdFromOAuth2User(oAuth2User, registrationId);

        String email = oAuth2ExtrationUtil.determineUserEmailFromOAuth2User(oAuth2User, registrationId, providerId);

        // If not present throw Error Reject service
        // If email not present in database then create new User and UserAuthProvider password null then genrate JWT
        // find email id and no data in UserAuthPorvider that means account not linked so link the account by storing UserAuthProvider info then genrate JWT
        // if email id is present user info in UserAuthProvider is present (check providerId and providerType because if user log in with different platform we need to save that info but under same user Id and email) then user alreday linked just genrate JWT.

        if (email == null || email.isBlank()) {
            throw new BadCredentialsException("Email Id is Missing :- Please provide Email info for Successful Login");
        }
        User user = userRepository.findByEmail(email).orElse(null);
        if(user==null){
            user = userRepository.save(User.builder()
                    .email(email)
                    .role(Role.USER)
                    .build());
            createAuthProvider(user,providerId, providerType);
        }else if(!userAuthProvidersRepository.existsByEmailAndProviderIdAndProviderType(email,providerId,providerType)){
            createAuthProvider(user,providerId, providerType);
        }

        // generate token here
        return new LoginResponseDTO(jwtUtilityService.generateToken(user), null);
    }

    private void createAuthProvider(User user, String providerId, ProviderType providerType){
        userAuthProvidersRepository.save(UserAuthProviders.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .providerId(providerId)
                .providerType(providerType)
                .build());
    }
}
