package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.service.LabelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/label")
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabel(@PathVariable Long id){

    }

}
