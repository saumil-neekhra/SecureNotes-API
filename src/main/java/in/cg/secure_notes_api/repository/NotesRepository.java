package in.cg.secure_notes_api.repository;

import in.cg.secure_notes_api.entity.Label;
import in.cg.secure_notes_api.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotesRepository extends JpaRepository<Notes, Long> {
}
