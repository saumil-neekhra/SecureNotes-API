package in.cg.secure_notes_api.repository;

import in.cg.secure_notes_api.entity.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    @Query("Select n from Notes n where n.isArchived = false and n.isEligibleForTrashCan = false and n.isPermanentlyDeleted = false")
    Optional<Notes> getNotesById(Long id);

    @Query("Select n from Notes n where n.isArchived = false and n.isEligibleForTrashCan = false and n.isPermanentlyDeleted = false")
    Page<Notes> getAllNotes(Pageable pageable);

    @Query("Select n from Notes n where n.isArchived = true")
    Page<Notes> getAllArchivedNotes(Pageable pageable);

    @Query("Select n from Notes n where n.isEligibleForTrashCan = true")
//    Only check this conditions because as a notes moves through different state
//    (isArchived, isEligibleForTrashCan, isPermanentlyDeleted) other state become false;
//    if isEligibleForTrashCan = true then isArchived = false and isPermanentlyDeleted = false;
//    if isPermanentlyDeleted = true others will be false;
    Page<Notes> getAllTrashedNotes(Pageable pageable);


}
