package in.cg.secure_notes_api.repository;

import in.cg.secure_notes_api.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
