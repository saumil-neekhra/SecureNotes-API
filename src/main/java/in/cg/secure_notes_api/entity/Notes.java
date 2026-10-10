package in.cg.secure_notes_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notesId;

    private Integer userId;

    private String title;

    @Lob
    private String content;

    private Boolean isArchived;

    @ManyToMany
    @JoinTable(
            name = "notes_label",
            joinColumns = @JoinColumn(name = "notesId"),
            inverseJoinColumns = @JoinColumn(name = "labelId")
    )

    private Set<Label> labels;

    private Boolean isMarkedDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updtedAt;


}
