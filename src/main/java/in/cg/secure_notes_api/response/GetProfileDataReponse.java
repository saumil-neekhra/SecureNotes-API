package in.cg.secure_notes_api.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GetProfileDataReponse {
    String email;
    Long userId;

}
