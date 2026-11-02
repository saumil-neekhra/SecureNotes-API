package in.cg.secure_notes_api.mapper;

import in.cg.secure_notes_api.dto.AddLabelToNotesResponseDTO;
import in.cg.secure_notes_api.dto.NotesRequestDTO;
import in.cg.secure_notes_api.dto.NotesResponseDTO;
import in.cg.secure_notes_api.dto.RemoveLabelFromNotesResponseDTO;
import in.cg.secure_notes_api.entity.Notes;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotesMapper {

    Notes toNotesEntity(NotesRequestDTO requestDTO);

    NotesResponseDTO toNotesDTO(Notes notes);

    AddLabelToNotesResponseDTO toAddLabelToNotesResponseDTO(Notes notes);

    RemoveLabelFromNotesResponseDTO toRemoveLabelFromNotesResponseDTO(Notes notes);
}
