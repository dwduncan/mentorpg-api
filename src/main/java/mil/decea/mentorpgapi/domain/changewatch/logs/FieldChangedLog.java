package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.NeverExpires;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.PreviousValueMessage;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.RecordFieldName;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedByStringComparison;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldChangedLog implements FieldChangedWatcher {

    @NotNull
    protected Long objectId;
    @NotNull
    protected String objectClass;
    @NotNull
    protected Long parentId;
    @NotNull
    protected String parentClass;

    protected String previousValue;

    protected boolean neverExpires;

    private boolean changed;

    public FieldChangedLog(Field field, TrackedEntity trackedObject, IdentifiedRecord incomingRecord, TrackedEntity parentObject){
        setValue(field,trackedObject,incomingRecord, parentObject);
    }

    public FieldChangedLog(Field field, TrackedEntity trackedObject, TrackedEntity incomingObject, TrackedEntity parentObject){
        setValue(field,trackedObject,incomingObject, parentObject);
    }

    public FieldChangedLog(String fieldName, TrackedEntity trackedObject, TrackedEntity parentObject){
        setValue(fieldName,trackedObject, parentObject);
    }


    void setValue(Field field,
                  TrackedEntity trackedObject,
                  IdentifiedRecord incomingRecord,
                  TrackedEntity parentObject){

        if (field == null || Collection.class.isAssignableFrom(field.getType())){
            return;
        }
        
        PreviousValueMessage pvm = field.getAnnotation(PreviousValueMessage.class);
        neverExpires = field.isAnnotationPresent(NeverExpires.class);
        
        try {
            RecordFieldName rfn = field.getAnnotation(RecordFieldName.class);
            field.setAccessible(true);
            String fieldNameIncoming = rfn == null ? field.getName() : rfn.value();
            Field fieldIncoming = incomingRecord.getClass().getDeclaredField(fieldNameIncoming);
            fieldIncoming.setAccessible(true);

            String before = getFieldValue(field, trackedObject);
            String incoming = getFieldValue(field, fieldIncoming, incomingRecord);

            changed = !Objects.equals(before, incoming);

            if (changed){

                //System.out.println(field.getName() + " Antes: " + before + "\t\t depois: " + incoming);

                if (pvm != null){
                    previousValue = pvm.value();
                }else{
                    previousValue = field.getName() +  ": " + before;
                }

                this.objectClass = trackedObject.getClass().getName();
                this.objectId = trackedObject.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
            }else{
                previousValue = null;
            }

        }catch (Exception ex){ex.printStackTrace();}
    }

    void setValue(Field field,
                  TrackedEntity trackedObject,
                  TrackedEntity incomingObject,
                  TrackedEntity parentObject){

        if (field == null || Collection.class.isAssignableFrom(field.getType())){
            return;
        }

        PreviousValueMessage pvm = field.getAnnotation(PreviousValueMessage.class);
        neverExpires = field.isAnnotationPresent(NeverExpires.class);

        try {

            Field fieldIncoming = incomingObject.getClass().getDeclaredField(field.getName());
            fieldIncoming.setAccessible(true);
            field.setAccessible(true);

            String before = getFieldValue(field, trackedObject);

            String incomingValue = getFieldValue(fieldIncoming, incomingObject);

            changed = Objects.equals(before, incomingValue);

            if (changed){

                if (pvm != null){
                    previousValue = pvm.value();
                }else{
                    previousValue = getFieldValue(field, trackedObject);
                }

                this.objectClass = trackedObject.getClass().getName();
                this.objectId = trackedObject.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
            }else{
                previousValue = null;
            }

        }catch (Exception ex){ex.printStackTrace();}
    }

    void setValue(String fieldName, TrackedEntity trackedObject, TrackedEntity parentObject){

        try{
            Field f = ReflectionUtils.getFieldByNameRecursively(trackedObject.getClass(),fieldName);
            PreviousValueMessage pvm = f == null ? null : f.getAnnotation(PreviousValueMessage.class);
            if (pvm != null){
                changed = true;
                previousValue = pvm.value();
                this.objectClass = trackedObject.getClass().getName();
                this.objectId = trackedObject.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
            }
        }catch (Exception ex){ex.printStackTrace();}

    }

    private String getFieldValue(Field field, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Object value = field.get(obj);

        if (value == null) return "";

        if (Temporal.class.isAssignableFrom(field.getType())){
            value = DateTimeAPIHandler.converter((Temporal) value);
        }

        return value.toString();
    }

    private String getFieldValue(Field trackedObjectField,Field field, IdentifiedRecord record) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {

        Object value = field.get(record);

        if (value == null) return "";

        TrackedByStringComparison tbsc = trackedObjectField.getType().getAnnotation(TrackedByStringComparison.class);

        if (tbsc != null && !tbsc.recordFieldToCompare().isBlank()){
            Method method = value.getClass().getMethod(tbsc.recordFieldToCompare());
            value = method.invoke(value);
            if (value == null) return "";
        }else if (Temporal.class.isAssignableFrom(field.getType())){
            value = DateTimeAPIHandler.converterStringDate(value.toString());
        }

        return value.toString();
    }


    @Override
    public String toString() {
        return previousValue;
    }
}
