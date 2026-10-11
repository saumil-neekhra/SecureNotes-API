package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.dto.AddLabelToNotesResponseDTO;
import in.cg.secure_notes_api.dto.RemoveLabelFromNotesResponseDTO;
import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.entity.Notes;
import in.cg.secure_notes_api.mapper.NotesMapper;
import in.cg.secure_notes_api.repository.LabelRepository;
import in.cg.secure_notes_api.repository.NotesRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;
import java.util.Set;

@Service
public class NotesService {

    private final NotesRepository notesRepository;
    private final LabelRepository labelRepository;
    private final NotesMapper notesMapper;

    public NotesService(NotesRepository notesRepository, LabelRepository labelRepository, NotesMapper notesMapper) {
        this.notesRepository = notesRepository;
        this.labelRepository = labelRepository;
        this.notesMapper = notesMapper;
    }

    public Notes createNotes(Notes notes, Principal principal){
        Notes newNotes = Notes.builder()
                .title(null)
                .content(null)
                .isArchived(false)
                .labels(null)
                .build();
        notesRepository.save(newNotes);
        return newNotes;
    }

    public Notes getNotes(Long id) {
        return notesRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Worng Id"));
    }

    public Page getAllNotes(Pageable pageable) {
        return notesRepository.findAll(pageable);
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
    public void changeNotesArchivedStatus(Long id) {
        notesRepository.findById(id).map(notes -> {
                    notes.setIsArchived(!notes.getIsArchived());
                    notesRepository.save(notes);
                    return notes;
                }
        ).orElseThrow(() -> new RuntimeException("Note not found with id: " + id));
    }

    @Transactional
    public AddLabelToNotesResponseDTO addLabelToNotes(Long notesId, Long labelId) {

        Notes notes = notesRepository.findById(notesId).orElseThrow(() ->
                new RuntimeException("Note not found: " + notesId)
        );
        Label label = labelRepository.findById(labelId).orElseThrow(() ->
                new RuntimeException("Label not founr: " + labelId));

        notes.getLabels().add(label);
        label.getNotes().add(notes);

        return notesMapper.toAddLabelToNotesResponseDTO(notesRepository.save(notes));
    }

    public RemoveLabelFromNotesResponseDTO removeLabelFromNotes(Long notesId, Long labelId) {
        Notes notes = notesRepository.findById(notesId).orElseThrow(() ->
                new RuntimeException("Note not found: " + notesId)
        );
        Label label = labelRepository.findById(labelId).orElseThrow(() ->
                new RuntimeException("Label not found: " + labelId));

        notes.getLabels().remove(label);
        label.getNotes().remove(notes);

        return notesMapper.toRemoveLabelFromNotesResponseDTO(notesRepository.save(notes));
    }

    public Set<Label> getAllLabelAssociatedWithNote(Long notesId) {
        Notes notes = notesRepository.findById(notesId).orElseThrow(() ->
                new RuntimeException("Note not found: " + notesId)
        );

        return notes.getLabels();
    }

    public void moveLabelToTrashCan(Long id) {
        Notes notes = notesRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Label not found: " + id));

        if (!notes.getIsEligibleForTrashCan()) {
            notes.setIsEligibleForTrashCan(true);
        } else {
            throw new RuntimeException("Already moved to trash Can");
        }

    }

    public void permanentlyDeleteFromUser(Long id) {
        Notes notes = notesRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Label not found: " + id));

        if (notes.getIsEligibleForTrashCan()) {
            notes.setIsMarkedDeleted(true);
            // A Schedular that will automatically delete this from database after 30 Days
        } else {
            throw new RuntimeException("Moved label to Trash Can First");
        }
    }
}
