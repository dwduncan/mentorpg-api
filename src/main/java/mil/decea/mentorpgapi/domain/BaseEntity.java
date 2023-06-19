package mil.decea.mentorpgapi.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class BaseEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    boolean ativo = true;



    public <T extends BaseEntity> void updateCollections(Collection<T> currentCollection, Collection<T> updatedCollection){
        currentCollection.clear();
        updatedCollection.forEach(e -> {
            if (e.getId() != null && e.getId() < 1) e.setId(null);
            currentCollection.add(e);
        });
    }

    public <T extends ExternalDataEntity<T>> void updateDocumentsCollections(Collection<T> currentCollection, Collection<T> updatedCollection){
        Map<Long, T> currentMap = currentCollection.stream().collect(Collectors.toMap(ExternalDataEntity::getId, Function.identity()));
        currentCollection.clear();
        updatedCollection.forEach(e -> {
            if (e.getId() != null && e.getId() < 1) e.setId(null);
            var prev = currentMap.get(e.getId());
            if (prev != null) {
                String oldFileName = prev.getNomeArquivo();
                prev.copyFields(e);
                prev.setPreviousFileName(oldFileName);
                currentCollection.add(prev);
            }else{
                currentCollection.add(e);
            }
        });
    }

}
