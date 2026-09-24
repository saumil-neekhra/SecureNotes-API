package in.cg.secure_notes_api.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateProfileResponse {
    String username;
    String message;
    String email;
}
