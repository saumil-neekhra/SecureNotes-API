package in.cg.secure_notes_api.dto;

import lombok.*;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@NoArgsConstructor
@Setter
public class UpdateLabelRequestDTO {
    Long id;
    private JsonNullable<String> labelName =
            JsonNullable.undefined();

    private JsonNullable<String> colour =
            JsonNullable.undefined();
}
