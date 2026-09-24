package in.cg.secure_notes_api.request;

import lombok.Builder;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
}
