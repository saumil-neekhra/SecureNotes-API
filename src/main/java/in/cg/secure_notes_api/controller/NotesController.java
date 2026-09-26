package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.repository.NotesRepository;
import in.cg.secure_notes_api.service.NotesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.security.URIParameter;
import java.util.List;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NotesRepository notesRepository;
    private final NotesService notesService;

    public NotesController(NotesRepository notesRepository, NotesService notesService) {
        this.notesRepository = notesRepository;
        this.notesService = notesService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> createNewNotes(@RequestBody Notes notes, Principal principal) {
        Notes savedNotes = notesService.createNotes(notes,principal);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedNotes.getNotesId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/id")
    public ResponseEntity<Notes> getNotes(@PathVariable Long id){
        Notes notes = notesService.getNotes(id);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("")
    public ResponseEntity<List<Notes>> getAllNotes(
            @PageableDefault (size = 10)
            @SortDefault( sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page page = notesService.getAllNotes(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    

}
