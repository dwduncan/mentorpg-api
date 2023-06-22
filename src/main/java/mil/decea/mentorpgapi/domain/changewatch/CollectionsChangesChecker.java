package mil.decea.mentorpgapi.domain.changewatch;

import lombok.Getter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.logs.CollectionChangedLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackChange;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedElementCollection;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CollectionsChangesChecker<T extends TrackedElementCollection<T>> {


    private final Collection<T> currentCollection;
    private final Collection<T> updatedCollection;
    private final TrackedEntity parentObject;
    private final List<FieldChangedWatcher> changesList;
    private final Class<?> elementType;


    public CollectionsChangesChecker(TrackedEntity parentObject, Collection<T> currentCollection, Collection<T> updatedCollection, Class<?> elementType, boolean updateCurrentList){
        this.currentCollection = currentCollection;
        this.updatedCollection = updatedCollection;
        this.elementType = elementType;
        this.parentObject = parentObject;
        changesList = new ArrayList<>(5);
        check(updateCurrentList);
    }

    public void check(boolean updateCurrentList){
        Map<Long, T> currentMap = currentCollection.stream().collect(Collectors.toMap(TrackedElementCollection::getId, Function.identity()));
        Map<Long, T> newMap = updatedCollection.stream().collect(Collectors.toMap(TrackedElementCollection::getId, Function.identity()));

        for(T curItem : currentMap.values()){
            T uptItem = newMap.remove(curItem.getId());            
            if (uptItem != null){//check if it has changes
                if (curItem.getClass().isAnnotationPresent(TrackChange.class)){
                    ObjectChangesChecker<T> occ = new ObjectChangesChecker<>(curItem, uptItem, parentObject);
                    changesList.addAll(occ.getChangesList());
                }
            }else{//the item was removed
                CollectionChangedLog _change = new CollectionChangedLog(curItem, null, parentObject);
                if (_change.isChanged()) changesList.add(_change);
            }
        }
        
        //check for new itens 
        newMap.values().forEach(newItem -> {
            CollectionChangedLog _change = new CollectionChangedLog(null, newItem, parentObject);
            if (_change.isChanged()) changesList.add(_change);
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
