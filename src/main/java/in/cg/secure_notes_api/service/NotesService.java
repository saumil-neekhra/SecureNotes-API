package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.repository.NotesRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class NotesService {

    private final NotesRepository notesRepository;

    public NotesService(NotesRepository notesRepository) {
        this.notesRepository = notesRepository;
    }

    public Notes createNotes(Notes notes, Principal principal){
        Notes newNotes = Notes.builder()
                .userId(1)
                .title(null)
                .content(null)
                .isArchived(false)
                .labelId(null)
                .labelName(null)
                .isMarkedDeleted(false)
                .build();
        notesRepository.save(newNotes);
        return newNotes;
    }

    public Notes getNotes(Long id) {
        return notesRepository.getReferenceById(id);
    }

    public Page getAllNotes(Pageable pageable) {
        return notesRepository.findAll(pageable);
    }

    public Boolean removeNotes(Long id) {
        if(notesExists(id)){
            notesRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public Boolean notesExists(Long id){
        return notesRepository.existsById(id);
    }

    public Optional<Notes> updateNotesUsingPut(Long id, Notes incomingNotes) {
        return notesRepository.findById(id).map( existingNotes -> {
            existingNotes.setTitle(incomingNotes.getTitle());
            existingNotes.setContent(incomingNotes.getContent());
            Notes savedNote = notesRepository.save(existingNotes);
            return savedNote;
        });


    }
}
