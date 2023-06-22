package mil.decea.mentorpgapi.domain.changewatch.logs;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import mil.decea.mentorpgapi.domain.user.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "changelogs", schema = "mentorpgapi",
    indexes = {@Index(name = "class_id_index", columnList = "objectClass, objectId")})
public class ChangeLog implements FieldChangedWatcher{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String previousValueOrMessage;

    @NotNull
    protected Long parentId;
    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String parentClass;

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

    public ChangeLog(FieldChangedWatcher o, Long idUser, String nome){
        previousValueOrMessage = o.getPreviousValueOrMessage();
        parentId = o.getParentId();
        parentClass = o.getParentClass();
        objectId = o.getObjectId();
        objectClass = o.getObjectClass();
        fieldName = o.getFieldName();
        fieldType = o.getFieldType();
        responsableId = idUser;
        responsableName = nome;
        occurrenceTime = LocalDateTime.now();
        neverExpires = o.isNeverExpires();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeLog changeLog)) return false;
        return Objects.equals(id, changeLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
