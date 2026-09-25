package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.repository.NotesRepository;
import in.cg.secure_notes_api.service.NotesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.URIParameter;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NotesRepository notesRepository;
    private final NotesService notesService;

    public NotesController(NotesRepository notesRepository, NotesService notesService) {
        this.notesRepository = notesRepository;
        this.notesService = notesService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createNewNotes(@RequestBody Notes notes) throws URISyntaxException {
        notesService.createNotes(notes);
        URI location;
        return ResponseEntity.notFound().build();
    }

}
