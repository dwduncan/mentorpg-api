package mil.decea.mentorpgapi.domain.changewatch;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(of = "id")
@Table(name = "zchangelogs", schema = "mentorpgapi",
    indexes = {@Index(name = "class_id_index", columnList = "objectClass, objectId")})
public class ZChangeLog implements FieldChangedWatcher{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(columnDefinition = "TEXT")
    protected String before;
    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String afterOrMessage;


    @NotNull
    protected Long objectId;
    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String objectClass;
    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String fieldName;
    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String fieldType;

    protected long responsableId;
    @Column(columnDefinition = "TEXT")
    @NotNull
    protected String responsableName;
    @Column(columnDefinition = "TIMESTAMP")
    @NotNull
    protected LocalDateTime occurrenceTime;

    protected boolean neverExpires = false;

    @NotNull
    @Column(columnDefinition = "TEXT")
    protected Long objectOwnerId;
    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String objectOwnerClass;


}
