package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.dto.AddLabelToNotesResponseDTO;
import in.cg.secure_notes_api.dto.RemoveLabelFromNotesResponseDTO;
import in.cg.secure_notes_api.entity.Label;
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
import java.util.Set;

@RestController
@RequestMapping("/notes")
public class NotesController {

    private final NotesRepository notesRepository;
    private final NotesService notesService;

    public NotesController(NotesRepository notesRepository, NotesService notesService) {
        this.notesRepository = notesRepository;
        this.notesService = notesService;
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

    @GetMapping("/{noteId}/labels")
    public ResponseEntity<Set<Label>> getAllLabelAssociatedWithNote(
            @PathVariable Long notesId
    ){
        return ResponseEntity.ok(notesService.getAllLabelAssociatedWithNote(notesId));
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

    @PostMapping("/{notesId}/labels/{labelId}")
    public ResponseEntity<AddLabelToNotesResponseDTO> addLabelToNotes(
            @PathVariable Long notesId,
            @PathVariable Long labelId){

        AddLabelToNotesResponseDTO savedNotes =
                notesService.addLabelToNotes(notesId, labelId);

        return ResponseEntity.ok(savedNotes);
    }

    @DeleteMapping("/{noteId}/labels/{labelId}")
    public ResponseEntity<RemoveLabelFromNotesResponseDTO> removeLabelFromNotes(
            @PathVariable Long notesId,
            @PathVariable Long labelId){

        RemoveLabelFromNotesResponseDTO removeLabelFromNotesResponseDTO = notesService.removeLabelFromNotes(notesId, labelId);
        return ResponseEntity.ok(removeLabelFromNotesResponseDTO);
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeNotes(@PathVariable Long id){

        notesService.moveLabelToTrashCan(id);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/delete-permanently/{id}")
    public ResponseEntity<Void> permanentlyDeleteFromUser(@PathVariable Long id){
        notesService.permanentlyDeleteFromUser(id);
        return ResponseEntity.noContent().build();
    }



    // Path for Auto-Save feature using debouncing and useEffect onChange in react
    // Debouncing dealying sending request of change by particular time so not to overwhelm the server

    @PutMapping("/update/{id}")
    public ResponseEntity<Notes> updateNotesUsingPut(@PathVariable Long id,
                                                     @RequestBody Notes notesDetail){
        Notes updatedNotes = notesService.updateNotesUsingPut(id, notesDetail).orElse(null);
        return ResponseEntity.ok(updatedNotes);
    }

    @PatchMapping("/{id}/archived-status")
    public ResponseEntity<Void> changeNotesArchivedStatus(@PathVariable Long id){
        notesService.changeNotesArchivedStatus(id);
        return ResponseEntity.noContent().build();
    }






}
