package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.repository.NotesRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotesController {

    private final NotesRepository notesRepository;

    public NotesController(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

}
