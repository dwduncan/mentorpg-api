package mil.decea.mentorpgapi.domain.changewatch;

import lombok.Getter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ObjectChangesChecker<T extends TrackedEntity<Z>, Z extends IdentifiedRecord> {


    private final T trackedObject;
    private Z changedRecord;
    private T changedObject;
    TrackedEntity<?> parentObject;
    private final List<FieldChanged> changesList;
    String[] onlyFieldsName;

    public ObjectChangesChecker(T trackedObject, Z changedRecord){
        this(trackedObject, changedRecord, null);
    }

    public ObjectChangesChecker(T trackedObject, T changedObject){
        this(trackedObject, changedObject, null);
    }

    public ObjectChangesChecker(T trackedObject, Z changedRecord, TrackedEntity<?> parentObject){
        this.trackedObject = trackedObject;
        this.changedRecord = changedRecord;
        this.parentObject = parentObject;
        changesList = new ArrayList<>(5);
        TrackChange trackChange = trackedObject.getClass().getAnnotation(TrackChange.class);
        onlyFieldsName = trackChange != null ? trackChange.onlyWithFieldsName() : null;

        if (parentObject != null || trackChange != null) {
            check();
        }
    }

    public ObjectChangesChecker(T trackedObject, T changedObject, TrackedEntity<?> parentObject){
        this.trackedObject = trackedObject;
        this.changedObject = changedObject;
        this.parentObject = parentObject;
        changesList = new ArrayList<>(5);
        TrackChange trackChange = trackedObject.getClass().getAnnotation(TrackChange.class);
        onlyFieldsName = trackChange != null ? trackChange.onlyWithFieldsName() : null;

        if (parentObject != null || trackChange != null) {
            check();
        }
    }

    private void check(){

        List<Field> allFields = ReflectionUtils.getAllFields(trackedObject.getClass(),true,onlyFieldsName);

        if (changedObject == null) {
            allFields.stream()
                    .filter(f -> !f.isAnnotationPresent(IgnoreTrackChange.class))
                    .forEach(this::trackField);
        }else{
            allFields.stream()
                    .filter(f -> !f.isAnnotationPresent(IgnoreTrackChange.class))
                    .forEach(field -> trackField(field,field));
        }
    }
    
    private void trackField(Field field){

        if (isReadable(field)){
            FieldChanged fc = new FieldChanged(field, trackedObject, changedRecord, parentObject);
            if (fc.isChanged()) changesList.add(fc);
        }else{
            try{
                Object _trackValue = field.get(trackedObject);
                Object _chgRec = changedRecord.getClass().getDeclaredField(field.getName()).get(changedRecord);
                
                if ((_chgRec instanceof IdentifiedRecord) && (_trackValue instanceof TrackedEntity<?>)) {
                    TrackedEntity<?> _parentObject = parentObject == null ? trackedObject : parentObject;
                    ObjectChangesChecker<?,?> ocb = new ObjectChangesChecker((TrackedEntity<?>) _trackValue,(IdentifiedRecord) _chgRec, _parentObject);
                    changesList.addAll(ocb.changesList);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void trackField(Field previousField, Field updatedField){

        if (isReadable(previousField)){
            FieldChanged fc = new FieldChanged(previousField, trackedObject, changedObject, parentObject);
            if (fc.isChanged()) changesList.add(fc);
        }else{
            try{
                Object _trackValue = previousField.get(trackedObject);
                Object _chgRec = updatedField.get(changedObject);

                if ((_chgRec instanceof TrackedEntity<?>) && (_trackValue instanceof TrackedEntity<?>)) {
                    TrackedEntity<?> _parentObject = parentObject == null ? trackedObject : parentObject;
                    ObjectChangesChecker<?,?> ocb = new ObjectChangesChecker((TrackedEntity<?>) _trackValue,(TrackedEntity) _chgRec, _parentObject);
                    changesList.addAll(ocb.changesList);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    static boolean isReadable(Field f){
        return f.isAnnotationPresent(InnerValueChange.class) || ReflectionUtils.isToStringReadable(f.getType());
    }


}
