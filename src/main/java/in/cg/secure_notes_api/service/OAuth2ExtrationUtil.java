package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.entity.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OAuth2ExtrationUtil {
    // Contains Methods For OAuth2
    // To fetch providerID and providerType

    public ProviderType getAuthProviderTypeFromRegistrationId(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> ProviderType.GOOGLE;
            case "github" -> ProviderType.GITHUB;
            case "facebook" -> ProviderType.FACEBOOK;
            case "twitter" -> ProviderType.TWITTER;
            default -> throw new IllegalArgumentException("Unsupported OAuth2 Provider: " + registrationId);
        };
    }

//    Every Site send ProviderId in some different way so we need special method to
//    get id like for Google attribute name is Sub in string format
//    For GitHub  it is id in long format so we use toString
    public String determineProviderIdFromOAuth2User(OAuth2User oAuth2User, String registrationId){
        String providerId = switch (registrationId.toLowerCase()){
            case "google" -> oAuth2User.getAttribute("sub");
            case "github" -> oAuth2User.getAttribute("id").toString();

            default -> throw new IllegalArgumentException("Unsupoorted OAuth2 provider: "+ registrationId);
        };
        if(providerId == null || providerId.isBlank()){
            log.error("Unable to determine providerId for provider: {}", registrationId);
            throw new IllegalArgumentException("Unable to determine providerId for OAuth2 login");
        }
        return  providerId;
    }

    public String determineUserEmailFromOAuth2User(OAuth2User oAuth2User, String registrationId, String providerId){
        String email = oAuth2User.getAttribute("email");
        if(email != null && !email.isBlank()) {
            return email;
        }
        return switch (registrationId.toLowerCase()){
                case "google" -> oAuth2User.getAttribute("sub");
                case "github" -> oAuth2User.getAttribute("login");
                default -> providerId;
        };
    }
}