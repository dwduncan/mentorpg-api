package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.InnerValueChange;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.NeverExpires;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.NoValueTrack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unchecked")
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
    @NotNull
    protected String fieldName;
    @NotNull
    protected String fieldType;

    protected String previousValueOrMessage;


    protected boolean neverExpires;

    private boolean changed;

    public FieldChangedLog(Field field, TrackedEntity beforeObj, IdentifiedRecord afterObj, TrackedEntity parentObject){
        setValue(field,beforeObj,afterObj, parentObject);
    }

    public FieldChangedLog(Field field, TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){
        setValue(field,beforeObj,afterObj, parentObject);
    }

    void setValue(Field field, TrackedEntity beforeObj, IdentifiedRecord afterObj, TrackedEntity parentObject){

        if (field == null || Collection.class.isAssignableFrom(field.getType())){
            return;
        }

        NoValueTrack nvTrack = field.getAnnotation(NoValueTrack.class);
        neverExpires = field.isAnnotationPresent(NeverExpires.class);

        try {
            this.fieldType = field.getType().getName();
            field.setAccessible(true);
            Field fieldAfter = afterObj.getClass().getDeclaredField(fieldName);
            fieldAfter.setAccessible(true);

            String before = getFieldValue(field, beforeObj);
            previousValueOrMessage = getFieldValue(fieldAfter, afterObj);

            changed = Objects.equals(before, previousValueOrMessage);

            if (changed){

                if (nvTrack != null){
                    previousValueOrMessage = nvTrack.value();
                }

                this.objectClass = beforeObj.getClass().getName();
                this.objectId = beforeObj.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
                this.fieldName = field.getName();
            }else{
                previousValueOrMessage = null;
            }

        }catch (Exception ex){ex.printStackTrace();}
    }

    void setValue(Field field, TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){

        if (field == null || Collection.class.isAssignableFrom(field.getType())){
            return;
        }

        NoValueTrack nvTrack = field.getAnnotation(NoValueTrack.class);
        neverExpires = field.isAnnotationPresent(NeverExpires.class);

        try {
            this.fieldType = field.getType().getName();
            field.setAccessible(true);
            Field fieldAfter = afterObj.getClass().getDeclaredField(fieldName);
            fieldAfter.setAccessible(true);

            String before = getFieldValue(field, beforeObj);
            previousValueOrMessage = getFieldValue(fieldAfter, afterObj);

            changed = Objects.equals(before, previousValueOrMessage);

            if (changed){

                if (nvTrack != null){
                    previousValueOrMessage = nvTrack.value();
                }

                this.objectClass = beforeObj.getClass().getName();
                this.objectId = beforeObj.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
                this.fieldName = field.getName();
            }else{
                previousValueOrMessage = null;
            }

        }catch (Exception ex){ex.printStackTrace();}
    }

    private String getFieldValue(Field field, Object obj) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        InnerValueChange ivc = field.getType().getAnnotation(InnerValueChange.class);
        Object value;
        if (ivc != null){
            boolean isMethod = ivc.value().endsWith("()");
            if (isMethod){
                value = field.getType().getDeclaredMethod(ivc.value()).invoke(obj);
            }else{
                value =field.getType().getDeclaredField(ivc.value()).get(obj);
            }
        }else{
            value = field.get(obj);
        }

        return value != null ? value.toString() : null;
    }
}
