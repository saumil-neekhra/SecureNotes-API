package in.cg.secure_notes_api.service;

import in.cg.secure_notes_api.dto.*;
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
    private final LabelService labelService;

    public NotesService(NotesRepository notesRepository, LabelRepository labelRepository, NotesMapper notesMapper, LabelService labelService) {
        this.notesRepository = notesRepository;
        this.labelRepository = labelRepository;
        this.notesMapper = notesMapper;
        this.labelService = labelService;
    }

    // get all active notes that are not archived  ---  Done
    // get all archived notes  ----  Done
    // get all trash notes  ----  Done
    // search includes archived notes   ----
    // Pinned Functionality Remaining
    // Update Functionality

    public NotesResponseDTO createNotes(NotesRequestDTO requestDTO, Principal principal){
        // Create using MapStruct and send DTO instead of Entity
        // Also check Principal (Authentication)  ??????????????????????????

        Notes savedNotes = notesMapper.toNotesEntity(requestDTO);
        notesRepository.save(savedNotes);

        return notesMapper.toNotesDTO(savedNotes);
    }

    public NotesResponseDTO getNotes(Long id) {
        // Sending Whole Notes instead send a DTO
         Notes notes = notesRepository.getNotesById(id)
                .orElseThrow(() -> new IllegalArgumentException("Note not Found : " + id));
         return notesMapper.toNotesDTO(notes);
    }

    public Page<NotesResponseDTO> getAllNotes(Pageable pageable) {
        // Send a DTO insted of sending a Entity
        notesRepository.findAll(pageable);

        return notesRepository
                .getAllNotes(pageable)
                .map(notesMapper::toNotesDTO);
//                .map(notes -> {
//            NotesResponseDTO notesDTO = notesMapper.toNotesDTO(notes);
//            return notesDTO;
//        });
    }

    @Transactional
    public AddLabelToNotesResponseDTO addLabelToNotes(Long notesId, AddLabelRequest addLabelRequest) {

        // Add label -> Notes
        // Id present -> Add
        // Id Absent -> Add Label -> Add it to Notes

        Notes notes = notesRepository.findById(notesId).orElseThrow(() ->
                new IllegalArgumentException("Note not found: " + notesId)
        );

        if(addLabelRequest.getLabelId() != null){
            Label label = labelRepository.findById(addLabelRequest.getLabelId())
                .orElseThrow(() ->
                new IllegalArgumentException("Label not found: " + addLabelRequest.getLabelId()));

            notes.getLabels().add(label);
            label.getNotes().add(notes);
        }else{
            CreateLabelRequest createLabelRequest = new CreateLabelRequest(addLabelRequest.getLabelName());
            CreateLabelResponse createLabelResponse = labelService.createLabel(createLabelRequest);

            Label label = labelRepository.findById(createLabelResponse.getLabelId())
                    .orElseThrow(() ->
                            new IllegalArgumentException("Label not found: " + createLabelResponse.getLabelId()));

            notes.getLabels().add(label);
            label.getNotes().add(notes);
        }

        return notesMapper.toAddLabelToNotesResponseDTO(notes);
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
        // No Issue
        Notes notes = notesRepository.findById(notesId).orElseThrow(() ->
                new IllegalArgumentException("Note not found: " + notesId)
        );
        return notes.getLabels();
    }

    @Transactional
    public NotesResponseDTO updateNotes(Long id,
                                        UpdateNotesRequestDTO requestDTO,
                                        Principal principal) {
        // Make Proper DTO Request and Response and Don't Send Optional to Controller
        // Treating NULL as don't Update
        Notes notes =  notesRepository
                .getNotesById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notes not found : " + id));

        if(requestDTO.getTitle()!=null){
            notes.setTitle(requestDTO.getTitle());
        }
        if(requestDTO.getContent()!=null){
            notes.setContent(requestDTO.getContent());
        }

        NotesResponseDTO savedNote = notesMapper.toNotesDTO(notes);
        return savedNote;
    }

    // Trying to save change by ourselves
    public void changeNotesArchivedStatus(ArchiveStatusRequestDTO requestDTO) {
        notesRepository.findById(requestDTO.getId()).map(notes -> {
                    notes.setIsArchived(requestDTO.getArchiveStatus());
                    notesRepository.save(notes);
                    return notes;
                }
        ).orElseThrow(() -> new RuntimeException("Note not found with id: " + requestDTO.getId()));
    }

    public Page<NotesResponseDTO> getAllArchivedNotes(Pageable pageable) {
        return notesRepository
                .getAllArchivedNotes(pageable)
                .map(notesMapper::toNotesDTO);
    }


    @Transactional
    // Transactional is used to save modified data persistently
    // If we don't do this then on Java Entity Object will be modified
    // but changes will not persist in database.
    public void moveNotesToTrashCan(Long id) {
        Notes notes = notesRepository
                .findById(id).orElseThrow(() -> new RuntimeException("Notes not found: " + id));

        if (!notes.getIsEligibleForTrashCan()) {
            notes.setIsEligibleForTrashCan(true);
            notes.setIsArchived(false);
        } else {
            throw new RuntimeException("Already moved to trash Can");
        }
    }

    public Page<NotesResponseDTO> getAllTrashedNotes(Pageable pageable) {
        return notesRepository.getAllTrashedNotes(pageable).map(notesMapper::toNotesDTO);
    }


    @Transactional
    public void permanentlyDeleteFromUser(Long id) {
        Notes notes = notesRepository
                .findById(id).orElseThrow(() -> new IllegalArgumentException("Label not found: " + id));

        if (notes.getIsEligibleForTrashCan()) {
            notes.setIsEligibleForTrashCan(false);
            notes.setIsMarkedDeleted(true);
            // A Schedular that will automatically delete this from database after 30 Days
        } else {
            throw new RuntimeException("Move the note to trash before permanently deleting it");
        }
    }






}
