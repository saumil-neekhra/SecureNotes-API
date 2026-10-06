package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.repository.LabelRepository;
import org.springframework.stereotype.Service;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }


    public Label getLabel(Long id) {
        return labelRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Worng Id"));
    }

    public Label createLabel(Label label) {
        Label newLabel = new Label();
        labelRepository.save(newLabel.builder()
                .labelName(label.getLabelName())
                .colour(label.getColour())
                .isMarkedDeleted(false)
                .build());
        return newLabel;
    }
}
