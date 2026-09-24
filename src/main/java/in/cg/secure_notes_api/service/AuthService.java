package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.dto.LoginRequestDTO;
import in.cg.secure_notes_api.dto.LoginResponseDTO;
import in.cg.secure_notes_api.dto.RegistrationResponseDTO;
import in.cg.secure_notes_api.dto.RegistrationRequestDTO;
import in.cg.secure_notes_api.entity.Role;
import in.cg.secure_notes_api.entity.User;
import in.cg.secure_notes_api.repository.UserRepository;
import in.cg.secure_notes_api.request.ChangePasswordRequest;
import in.cg.secure_notes_api.request.UpdateProfileRequest;
import in.cg.secure_notes_api.response.ChangePasswordResponse;
import in.cg.secure_notes_api.response.GetProfileDataReponse;
import in.cg.secure_notes_api.response.UpdateProfileResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtilityService jwtUtilityService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtilityService jwtUtilityService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtilityService = jwtUtilityService;
    }

    public RegistrationResponseDTO register(RegistrationRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already taken");
        }
        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        return RegistrationResponseDTO.builder()
                .message("Created Successfully")
                .email(user.getEmail())
                .timeStamp(LocalDateTime.now())
                .build();

    }

    public LoginResponseDTO login(LoginRequestDTO dto){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

//        we are checking again because we want correct data of user from database.
//        if (!userRepository.existsByEmail(dto.getEmail())) {
//            throw new IllegalArgumentException("Email already taken");
//        }
//        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());
//        User user = (User) optionalUser.orElse(null);
            String token = jwtUtilityService.generateToken(user);
            return LoginResponseDTO.builder()
                    .accessToken(token)
                    .message("Login Successful")
                    .build();
    }

    public ChangePasswordResponse changePassword(ChangePasswordRequest request) {
        User u1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = u1.getEmail();
        User user = (User) userRepository.findByEmail(email).orElse(null);
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())){
            throw new BadCredentialsException("Enter Correct Credentials");
        }
        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
            throw new IllegalArgumentException("New password cannot be the same as old password");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        return ChangePasswordResponse.builder()
                .messaage("Password Changed Successfully").build();
    }

    public GetProfileDataReponse getProfileData() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return GetProfileDataReponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }


//    public UpdateProfileResponse updateProfile(UpdateProfileRequest request) {
//        User u1 = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String email = u1.getEmail();
//        User user = (User) userRepository.findByEmail(email).orElse(null);
//
//
//    }
}
