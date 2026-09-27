package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.repository.NotesRepository;
import in.cg.secure_notes_api.service.NotesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
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

    @GetMapping("/")
    public ResponseEntity<List<Notes>> getAllNotes(
            @PageableDefault (size = 10)
            @SortDefault( sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page page = notesService.getAllNotes(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeNotes(@PathVariable Long id){
        if(notesService.removeNotes(id)){
            return ResponseEntity.accepted().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Path for Auto-Save feature using debouncing and useEffect onChange in react
    // Debouncing dealying sending request of change by particular time so not to overwhlem the server

    @PutMapping("/save/{id}")
    public ResponseEntity<Notes> updateNotesUsingPut(@PathVariable Long id,
                                                     @RequestBody Notes notesDetail){
        Notes updatedNotes = notesService.updateNotesUsingPut(id, notesDetail).orElse(null);
        return ResponseEntity.ok(updatedNotes);
    }



}
