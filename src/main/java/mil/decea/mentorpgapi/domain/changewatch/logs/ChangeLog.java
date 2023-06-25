package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "changelogs", schema = "mentorpgapi")
public class ChangeLog implements FieldChangedWatcher{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String previousValue;

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
    protected Long responsableId;

    @NotNull
    @Column(columnDefinition = "TEXT")
    protected String responsableName;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime occurrenceTime;

    protected boolean neverExpires = false;

    public ChangeLog(FieldChangedWatcher o, @NotNull Long idUser,  @NotNull String nome){
        previousValue = o.getPreviousValue();
        parentId = o.getParentId();
        parentClass = o.getParentClass().replaceFirst("\\$.+","");
        objectId = o.getObjectId();
        objectClass = o.getObjectClass().replaceFirst("\\$.+","");
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
