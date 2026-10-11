package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.dto.*;
import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.mapper.LabelMapper;
import in.cg.secure_notes_api.repository.LabelRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

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

    @Transactional
    public UpdateLabelResponseDTO updateLabel(
            UpdateLabelRequestDTO requestDTO, Long id) {

        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Label not found"));

        if (requestDTO.getLabelName().isPresent()) {
            String name = requestDTO.getLabelName().get();

            if (name == null || name.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Label name cannot be null or blank");
            }

            label.setLabelName(name);
        }

        if (requestDTO.getColour().isPresent()) {
            String colour = requestDTO.getColour().get();

            if (colour == null || colour.isBlank()) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Colour cannot be null or blank");
            }

            label.setColour(colour);
        }

        return UpdateLabelResponseDTO.builder()
                .id(label.getLabelId())
                .labelName(label.getLabelName())
                .colour(label.getColour())
                .build();
    }
}
