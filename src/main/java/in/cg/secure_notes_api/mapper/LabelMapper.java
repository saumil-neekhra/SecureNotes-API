package in.cg.secure_notes_api.mapper;

import in.cg.secure_notes_api.dto.CreateLabelRequest;
import in.cg.secure_notes_api.dto.CreateLabelResponse;
import in.cg.secure_notes_api.dto.GetLabelResponseDTO;
import in.cg.secure_notes_api.entity.Label;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    Label toEntity(CreateLabelRequest createLabelRequest);

    CreateLabelResponse toResponse(Label label);

    GetLabelResponseDTO toGetLabelResponseDTO(Label label);
}
