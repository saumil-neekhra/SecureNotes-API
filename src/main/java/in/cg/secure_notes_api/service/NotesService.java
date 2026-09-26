package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.repository.NotesRepository;
import org.aspectj.weaver.ast.Not;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public Notes createNotes(Notes notes, Principal principal){
        Notes newNotes = Notes.builder()
                .userId(1)
                .heading(null)
                .Data(null)
                .isArchived(false)
                .labelId(null)
                .labelName(null)
                .isMarkedDeleted(false)
                .build();
        notesRepository.save(newNotes);
        return newNotes;
    }

    public Notes getNotes(Long id) {
    }

    public Page getAllNotes(Pageable pageable) {
    }
}
