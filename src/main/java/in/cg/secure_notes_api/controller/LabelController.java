package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.repository.LabelRepository;
import in.cg.secure_notes_api.service.LabelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/label")
public class LabelController {
    private final LabelRepository labelRepository;
    private final LabelService labelService;

    public LabelController(LabelRepository labelRepository, LabelService labelService) {
        this.labelRepository = labelRepository;
        this.labelService = labelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Label> getLabel(@PathVariable Long id){
        Label label = labelService.getLabel(id);
        if(label!=null){
            return ResponseEntity.ok(label);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Void> createLabel(@RequestBody Label label){
        Label newLabel = labelService.createLabel(label);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLabel.getLabelId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping



}
