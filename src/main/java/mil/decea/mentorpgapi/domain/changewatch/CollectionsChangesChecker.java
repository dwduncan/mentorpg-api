package mil.decea.mentorpgapi.domain.changewatch;

import lombok.Getter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.logs.ObjecCreatedLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.ObjecRemovedLog;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackOnlySelectedFields;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedElementCollection;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CollectionsChangesChecker<T extends TrackedElementCollection<T>> extends ArrayList<FieldChangedWatcher> {

    private final Collection<T> currentCollection;
    private final Collection<T> updatedCollection;
    private final TrackedEntity parentObject;
    private final Class<?> elementType;

    public CollectionsChangesChecker(TrackedEntity parentObject, Collection<T> currentCollection, Collection<T> updatedCollection, Class<?> elementType, boolean updateCurrentList){
        super(5);
        this.currentCollection = currentCollection;
        this.updatedCollection = updatedCollection;
        this.elementType = elementType;
        this.parentObject = parentObject;
        check(updateCurrentList);
    }

    public void check(boolean updateCurrentList){
        Map<Long, T> currentMap = currentCollection.stream().collect(Collectors.toMap(TrackedElementCollection::getId, Function.identity()));
        Map<Long, T> newMap = updatedCollection.stream().collect(Collectors.toMap(TrackedElementCollection::getId, Function.identity()));

        for(T curItem : currentMap.values()){
            T uptItem = newMap.remove(curItem.getId());            
            if (uptItem != null){//check if it has changes
                if (curItem.getClass().isAnnotationPresent(TrackOnlySelectedFields.class)){
                    ObjectChangesChecker occ = new ObjectChangesChecker(curItem, uptItem, parentObject);
                    addAll(occ);
                }
            }else{//the item was removed
                add(new ObjecRemovedLog(curItem,  parentObject, true));
            }
        }
        
        //check for new itens 
        newMap.values().forEach(newItem -> {
            add(new ObjecCreatedLog(newItem, parentObject));
        });
        
        
        if (updateCurrentList){
            
            boolean externalData = ExternalDataEntity.class.isAssignableFrom(elementType);

            currentCollection.clear();

            updatedCollection.forEach(e -> {
                if (e.getId() != null && e.getId() < 1) e.setId(null);
                var prev = currentMap.get(e.getId());
                if (prev != null) {
                    if (externalData) {
                        String oldFileName = ((ExternalDataEntity) prev).getNomeArquivo();
                        prev.copyFields(e);
                        ((ExternalDataEntity) prev).setPreviousFileName(oldFileName);
                        currentCollection.add(prev);
                    } else {
                        prev.copyFields(e);
                        currentCollection.add(e);
                    }
                }
            });
        }
    }
}
