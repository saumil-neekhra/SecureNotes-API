package in.cg.secure_notes_api.repository;

import in.cg.secure_notes_api.entity.Label;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {
    Page findAllByIdAndMarkedDeletedFalse(Pageable pageable);
    Optional<Label> findByIdAndMarkedDeletedFalse(Long id);
}
