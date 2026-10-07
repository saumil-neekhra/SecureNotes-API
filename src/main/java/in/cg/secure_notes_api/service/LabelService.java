package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.repository.LabelRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Optional;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }


    public Label getLabel(Long id) {
        if(labelRepository.existsById(id)){
            return labelRepository.findByIdAndMarkedDeletedFalse(id).get();
        }
        throw new IllegalArgumentException("Worng Id");
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

    public Optional<Label> updateLabel(Label label, Long id) {
        return labelRepository.findById(id).map(existingLabel -> {
                existingLabel.setLabelName(label.getLabelName());
                existingLabel.setColour(label.getColour());
                Label savedlabel = labelRepository.save(existingLabel);
                return savedlabel;
        });
    }


    public boolean deleteLabel(Long id) {
        if(labelRepository.existsById(id)){
            Label selectedLabel = labelRepository.findById(id).get();
            if(!selectedLabel.getIsMarkedDeleted()){
                selectedLabel.setIsMarkedDeleted(false);
                labelRepository.save(selectedLabel);
                return false;
            }else{
                throw new IllegalArgumentException("Id Already got deleted");
            }
        }else{
            throw new IllegalArgumentException("Id Does Not Exists");
        }
    }

    public Page getAllLabel(Pageable pageable) {
        return labelRepository.findAllByIdAndMarkedDeletedFalse(pageable);
    }
}
