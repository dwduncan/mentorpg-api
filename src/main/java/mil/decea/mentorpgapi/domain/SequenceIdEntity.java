package mil.decea.mentorpgapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.CollectionsChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.IgnoreTrackChange;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedElementCollection;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class SequenceIdEntity implements TrackedEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    boolean ativo = true;

    @IgnoreTrackChange
    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime lastUpdate;

    public <T extends TrackedElementCollection<T>> CollectionsChangesChecker<T> updateDocumentsCollections(Collection<T> currentCollection, Collection<T> updatedCollection, Class<?> elementType, boolean updateCurrentList){

        CollectionsChangesChecker<T> ccc = new CollectionsChangesChecker<>(this, currentCollection, updatedCollection, elementType, updateCurrentList);
        return ccc;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SequenceIdEntity sequenceIdEntity)) return false;
        return Objects.equals(id, sequenceIdEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
