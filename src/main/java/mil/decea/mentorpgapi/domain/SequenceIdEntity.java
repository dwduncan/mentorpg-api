package mil.decea.mentorpgapi.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import mil.decea.mentorpgapi.domain.changewatch.CollectionChanged;
import mil.decea.mentorpgapi.domain.changewatch.CollectionsChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.TrackedElementCollection;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class SequenceIdEntity<Z extends IdentifiedRecord> implements TrackedEntity<Z> {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    boolean ativo = true;



    /*public <T extends BaseEntity> void updateCollections(Collection<T> currentCollection, Collection<T> updatedCollection){
        currentCollection.clear();
        updatedCollection.forEach(e -> {
            if (e.getId() != null && e.getId() < 1) e.setId(null);
            currentCollection.add(e);
        });
    }*/

    public <T extends TrackedElementCollection<T>> List<FieldChangedWatcher> updateDocumentsCollections(Collection<T> currentCollection, Collection<T> updatedCollection, Class<?> elementType, boolean updateCurrentList){

        CollectionsChangesChecker<T> ccc = new CollectionsChangesChecker<>(this, currentCollection, updatedCollection, elementType, updateCurrentList);
        return ccc.getChangesList();
    }




}
