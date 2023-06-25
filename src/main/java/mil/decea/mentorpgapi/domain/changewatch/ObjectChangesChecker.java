package mil.decea.mentorpgapi.domain.changewatch;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.logs.ObjecCreatedLog;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.IgnoreTrackChange;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.NotAutomatedTrack;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackOnlySelectedFields;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedByStringComparison;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor
public class ObjectChangesChecker extends ArrayList<FieldChangedWatcher>{

    private TrackedEntity trackedObject;
    private IdentifiedRecord incomingRecord;
    private TrackedEntity incomingObject;
    TrackedEntity parentObject;
    String[] onlyFieldsName;

    public ObjectChangesChecker(TrackedEntity trackedObject, IdentifiedRecord incomingRecord, TrackedEntity parentObject){
        super(1);
        this.trackedObject = trackedObject;
        this.incomingRecord = incomingRecord;
        this.parentObject = parentObject;
        TrackOnlySelectedFields trackOnlySelectedFields = trackedObject.getClass().getAnnotation(TrackOnlySelectedFields.class);
        onlyFieldsName = trackOnlySelectedFields != null ? trackOnlySelectedFields.value() : null;

        if (trackedObject.getId() == null){
            add(new ObjecCreatedLog(trackedObject, parentObject));
        }else {
            check();
        }
    }

    public ObjectChangesChecker(TrackedEntity trackedObject,
                                TrackedEntity incomingObject,
                                TrackedEntity parentObject){

        super(1);
        this.trackedObject = trackedObject;
        this.incomingObject = incomingObject;
        this.parentObject = parentObject;

        TrackOnlySelectedFields trackOnlySelectedFields = trackedObject.getClass().getAnnotation(TrackOnlySelectedFields.class);
        onlyFieldsName = trackOnlySelectedFields != null ? trackOnlySelectedFields.value() : null;

        if (trackedObject.getId() == null){
            add(new ObjecCreatedLog(trackedObject, parentObject));
        }else {
            check();
        }
    }

    private void check(){

        Set<Field> allFields = ReflectionUtils.getAllFields(
                trackedObject.getClass(),
                true,
                onlyFieldsName);

        if (incomingObject == null) {

           allFields.stream()
                    .filter(f -> !f.isAnnotationPresent(IgnoreTrackChange.class)
                            && !f.isAnnotationPresent(Transient.class)
                            && !f.isAnnotationPresent(NotAutomatedTrack.class)).forEach(this::trackField);
        }else{
            allFields.stream()
                    .filter(f -> !f.isAnnotationPresent(IgnoreTrackChange.class)
                            && !f.isAnnotationPresent(Transient.class)
                            && !f.isAnnotationPresent(NotAutomatedTrack.class))
                    .forEach(field -> trackField(field,field));
        }

    }
    
    private void trackField(Field field){

        if (isReadable(field)){
            FieldChangedLog fc = new FieldChangedLog(field, trackedObject, incomingRecord, parentObject);
            if (fc.isChanged()) {
                add(fc);
            }
        }else {
            try {
                if (TrackedEntity.class.isAssignableFrom(field.getType())) {
                    Field incomingField = incomingRecord.getClass().getDeclaredField(field.getName() + "Record");
                    TrackedEntity previousFieldValue = (TrackedEntity) field.get(trackedObject);
                    IdentifiedRecord incomingFieldValue = (IdentifiedRecord) incomingField.get(incomingRecord);
                    TrackedEntity _parentObject = parentObject == null ? trackedObject : parentObject;
                    addAll(new ObjectChangesChecker(previousFieldValue,incomingFieldValue, _parentObject));
                }
            } catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void trackField(Field previousField, Field incomingField){

        if (isReadable(previousField)){
            FieldChangedLog fc = new FieldChangedLog(previousField, trackedObject, incomingObject, parentObject);
            if (fc.isChanged()) {
                add(fc);
            }
        }else{
            try{
                Object _trackValue = previousField.get(trackedObject);
                Object _chgRec = incomingField.get(incomingObject);

                if ((_chgRec instanceof TrackedEntity) && (_trackValue instanceof TrackedEntity)) {
                    TrackedEntity _parentObject = parentObject == null ? trackedObject : parentObject;
                    addAll(new ObjectChangesChecker((TrackedEntity) _trackValue,(TrackedEntity) _chgRec, _parentObject));
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    static boolean isReadable(Field f){
        return f.getType().isAnnotationPresent(TrackedByStringComparison.class) ||
                ReflectionUtils.isToStringReadable(f.getType());
    }


    /**
     * Always set the object id like setObjectId does, however it will set the parent id only
     * if objectClass and parentClass are equal.
     * @param id
     */
    public void setObjectAndParentIdIfEquals(Long id){

        forEach(log -> {
            log.setObjectId(id);
            if (Objects.equals(log.getObjectClass(), log.getParentClass())){
                log.setParentId(id);
            }
        });

    }

}
