package in.cg.secure_notes_api.controller;

import in.cg.secure_notes_api.dto.*;
import in.cg.secure_notes_api.entity.Label;
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

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotesResponseDTO> getNotes(@PathVariable Long id){
        NotesResponseDTO notes = notesService.getNotes(id);
        return ResponseEntity.ok(notes);
    }

    @GetMapping("")
    public ResponseEntity<List<NotesResponseDTO>> getAllNotes(
            @PageableDefault (size = 10)
            @SortDefault( sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        Page<NotesResponseDTO> page = notesService.getAllNotes(pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/{notesId}/labels")
    public ResponseEntity<Set<Label>> getAllLabelAssociatedWithNote(
            @PathVariable Long notesId
    ){
        return ResponseEntity.ok(notesService.getAllLabelAssociatedWithNote(notesId));
    }

    @PostMapping("")
    public ResponseEntity<NotesResponseDTO> createNewNotes(@RequestBody NotesRequestDTO request, Principal principal) {
        NotesResponseDTO response = notesService.createNotes(request,principal);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.getNotesId())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{notesId}/labels/{labelId}")
    public ResponseEntity<AddLabelToNotesResponseDTO> addLabelToNotes(
            @PathVariable Long notesId,
            @PathVariable Long labelId){

        AddLabelToNotesResponseDTO savedNotes =
                notesService.addLabelToNotes(notesId, labelId);

        return ResponseEntity.ok(savedNotes);
    }

    @DeleteMapping("/{notesId}/labels/{labelsId}")
    public ResponseEntity<RemoveLabelFromNotesResponseDTO> removeLabelFromNotes(
            @PathVariable Long notesId,
            @PathVariable Long labelsId){

        RemoveLabelFromNotesResponseDTO removeLabelFromNotesResponseDTO =
                notesService.removeLabelFromNotes(notesId, labelsId);
        return ResponseEntity.ok(removeLabelFromNotesResponseDTO);
    }


    @DeleteMapping("/notes/{noteId}/trash")
    public ResponseEntity<Void> moveNotesToTrashCan(@PathVariable Long noteId){

        notesService.moveNotesToTrashCan(noteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/notes/{noteId}/permanent")
    public ResponseEntity<Void> permanentlyDeleteFromUser(@PathVariable Long noteId){
        notesService.permanentlyDeleteFromUser(noteId);
        return ResponseEntity.noContent().build();
    }



    // Path for Auto-Save feature using debouncing and useEffect onChange in react
    // Debouncing dealying sending request of change by particular time so not to overwhelm the server

    @PutMapping("/update/{noteId}")
    public ResponseEntity<NotesResponseDTO> updateNotesUsingPut(@PathVariable Long noteId,
                                                     @RequestBody UpdateNotesRequestDTO requestDTO,
                                                     Principal principal){
        NotesResponseDTO responseDTO = notesService.updateNotesUsingPut(noteId, requestDTO, principal);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/archive")
    public ResponseEntity<Void> changeNotesArchivedStatus(@RequestBody ArchiveStatusRequestDTO archiveStatusRequestDTO){
        notesService.changeNotesArchivedStatus(archiveStatusRequestDTO);
        return ResponseEntity.noContent().build();
    }

    //get all archived notes
    @GetMapping("/archived-notes/")
    public ResponseEntity<Page<NotesResponseDTO>> getAllArchivedNotes(
            @PageableDefault (size = 10)
                @SortDefault ( sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(notesService.getAllArchivedNotes(pageable));
    }


    // get all trashed Notes
    @GetMapping("/trashed-notes/")
    public ResponseEntity<Page<NotesResponseDTO>> getAllTrashedNotes(
            @PageableDefault (size = 10)
            @SortDefault( sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        return ResponseEntity.ok(notesService.getAllTrashedNotes(pageable));
    }


}
