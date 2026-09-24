package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.dto.LoginRequestDTO;
import in.cg.secure_notes_api.dto.LoginResponseDTO;
import in.cg.secure_notes_api.dto.RegistrationResponseDTO;
import in.cg.secure_notes_api.dto.RegistrationRequestDTO;
import in.cg.secure_notes_api.request.ChangePasswordRequest;
import in.cg.secure_notes_api.response.ChangePasswordResponse;
import in.cg.secure_notes_api.response.GetProfileDataReponse;
import in.cg.secure_notes_api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserAuthController {

    //-- Put -> for Complete Profile Update
    //-- Patch -> for Partial resource modification

    private final AuthService authService;

    public UserAuthController(AuthService authService) {
        this.authService = authService;
    }

    //-- Authentication --//

    @PostMapping("/auth/register")
    public ResponseEntity<RegistrationResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("auth/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.login(dto));
    }

    //---  User Module  ---//

    @PostMapping("user/changePassword")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.changePassword(changePasswordRequest));
    }

    @GetMapping("user/viewProfile")
    public ResponseEntity<GetProfileDataReponse> getProfileData(){
        return ResponseEntity.status(HttpStatus.OK).body(authService.getProfileData());

    }
//    @PatchMapping("user/updateProfile")
//    public ResponseEntity<UpdateProfileResponse> updateProfile(@RequestBody UpdateProfileRequest request){
//        return ResponseEntity.status(HttpStatus.OK).body(authService.updateProfile(request));
//    }

}
