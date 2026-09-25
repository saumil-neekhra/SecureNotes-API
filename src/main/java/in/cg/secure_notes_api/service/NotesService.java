package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.repository.NotesRepository;
import org.springframework.stereotype.Service;

@Service
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public void createNotes()
}
