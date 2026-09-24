package in.cg.secure_notes_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long notesId;

    private int userId;

    private String heading;

    private String Data;

    private Boolean isArchived;

    private int labelId;

    private int labelName;

    private Boolean isMarkedDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updtedAt;


}
