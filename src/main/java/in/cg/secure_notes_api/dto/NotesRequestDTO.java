package in.cg.secure_notes_api.dto;

import in.cg.secure_notes_api.entity.Label;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotesRequestDTO {
    String title;
    String content;
    Set<Label> labels;
}
