package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.repository.LabelRepository;
import in.cg.secure_notes_api.service.LabelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.awt.print.Pageable;
import java.net.URI;
import java.util.List;

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

    @GetMapping("/")
    public ResponseEntity<List<Label>> getAllLabel(
            @PageableDefault (size = 10)
            @SortDefault( sort = "labelId", direction = Sort.Direction.DESC) Pageable pageable
            ){
        Page page = labelService.getAllLabel(pageable);
        return ResponseEntity.ok(page.getContent());
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

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updateLabel(@RequestBody Label label, @PathVariable Long id){
        labelService.updateLabel(label, id);
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id){
        if(labelService.deleteLabel(id)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
