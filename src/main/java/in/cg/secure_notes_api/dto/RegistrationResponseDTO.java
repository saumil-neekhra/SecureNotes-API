package in.cg.secure_notes_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponseDTO {
    String email;
    String message;
    LocalDateTime timeStamp;
}
