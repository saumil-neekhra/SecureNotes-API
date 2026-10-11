package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.dto.*;
import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.repository.LabelRepository;
import in.cg.secure_notes_api.service.LabelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/labels")
public class LabelController {
    private final LabelRepository labelRepository;
    private final LabelService labelService;

    public LabelController(LabelRepository labelRepository, LabelService labelService) {
        this.labelRepository = labelRepository;
        this.labelService = labelService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetLabelResponseDTO> getLabel(@PathVariable Long id){
        GetLabelResponseDTO response = labelService.getLabel(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<GetLabelResponseDTO>> getAllLabel(
            @PageableDefault (size = 10)
            @SortDefault( sort = "labelId", direction = Sort.Direction.DESC) Pageable pageable
            ){
        Page page = labelService.getAllLabel(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/{labelId}/notes")
    public ResponseEntity<Set<Notes>> getAllNotesAssociatedWithLabel(
            @PathVariable Long labelId
    ){
        return ResponseEntity.ok(labelService.getAllNotesAssociatedWithLabel(labelId));
    }

    @PostMapping("/")
    public ResponseEntity<CreateLabelResponse> createLabel(@RequestBody CreateLabelRequest label){
        CreateLabelResponse newLabel = labelService.createLabel(label);
//        SEO Friendly Application should send url to get to the created resources
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newLabel.getLabelId())
                .toUri();
        return ResponseEntity.created(location).body(newLabel);
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<UpdateLabelResponseDTO> updateLabel(
            @RequestBody UpdateLabelRequestDTO label,
            @PathVariable Long id) {

        UpdateLabelResponseDTO response =
                labelService.updateLabel(label, id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id){
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }


}
