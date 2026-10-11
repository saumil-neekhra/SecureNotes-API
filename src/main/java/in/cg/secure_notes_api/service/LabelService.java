package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.dto.*;
import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.mapper.LabelMapper;
import in.cg.secure_notes_api.repository.LabelRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.Set;

@Service
public class LabelService {
    private final LabelRepository labelRepository;
    private final LabelMapper labelMapper;

    public LabelService(LabelRepository labelRepository, LabelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.labelMapper = labelMapper;
    }

    public GetLabelResponseDTO getLabel(Long id) {
        GetLabelResponseDTO responseDTO = labelMapper.toGetLabelResponseDTO(labelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Label not Found: " + id)));
        return responseDTO;
    }

    public Page<GetLabelResponseDTO> getAllLabel(Pageable pageable) {
        Page<Label> labelPage = labelRepository.findAll(pageable);
        Page<GetLabelResponseDTO> labelResponseDTOPage =
                labelPage.map(label -> labelMapper.toGetLabelResponseDTO(label));
        return labelResponseDTOPage;
    }

    public CreateLabelResponse createLabel(CreateLabelRequest request) {
        Label newlabel = labelMapper.toEntity(request);
        Label savedLabel = labelRepository.save(newlabel);
        CreateLabelResponse response = labelMapper.toResponse(savedLabel);
        return response;
    }

    public UpdateLabelResponseDTO updateLabel(UpdateLabelRequestDTO requestDTO, Long id) {
        return labelRepository.findById(id).map(existingLabel -> {
                existingLabel.setLabelName(requestDTO.getLabelName());
                existingLabel.setColour(requestDTO.getColour());
                Label savedlabel = labelRepository.save(existingLabel);
                return savedlabel;
        });
    }

    public Set<Notes> getAllNotesAssociatedWithLabel(Long labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new RuntimeException("Label not found: " + labelId));

        return label.getNotes();
    }


    public void deleteLabel(Long labelId) {
        if(labelRepository.existsById(labelId)){
            labelRepository.deleteById(labelId);
        }else{
            throw new RuntimeException("Label not found: " + labelId);
        }
    }
}
