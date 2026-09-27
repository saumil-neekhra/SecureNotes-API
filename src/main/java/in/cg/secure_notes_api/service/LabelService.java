package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.repository.LabelRepository;
import jdk.jfr.Label;
import org.springframework.stereotype.Service;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }


}
