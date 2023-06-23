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

    protected String changeMessage;


    protected boolean neverExpires;

    private boolean changed;

    public FieldChangedLog(Field field, TrackedEntity beforeObj, IdentifiedRecord afterObj, TrackedEntity parentObject){
        setValue(field,beforeObj,afterObj, parentObject);
    }

    public FieldChangedLog(Field field, TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){
        setValue(field,beforeObj,afterObj, parentObject);
    }


    public FieldChangedLog(String fieldName, TrackedEntity beforeObj, TrackedEntity parentObject){
        setValue(fieldName,beforeObj, parentObject);
    }


    void setValue(Field field, TrackedEntity beforeObj, IdentifiedRecord afterObj, TrackedEntity parentObject){

        if (field == null || Collection.class.isAssignableFrom(field.getType())){
            return;
        }

        NoValueTrack nvTrack = field.getAnnotation(NoValueTrack.class);
        neverExpires = field.isAnnotationPresent(NeverExpires.class);

        try {
            field.setAccessible(true);
            Field fieldAfter = afterObj.getClass().getDeclaredField(field.getName());
            fieldAfter.setAccessible(true);

            String before = getFieldValue(field, beforeObj);
            changeMessage = getFieldValue(fieldAfter, afterObj);

            changed = Objects.equals(before, changeMessage);

            if (changed){

                if (nvTrack != null){
                    changeMessage = nvTrack.value();
                }

                this.objectClass = beforeObj.getClass().getName();
                this.objectId = beforeObj.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
            }else{
                changeMessage = null;
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
            field.setAccessible(true);
            Field fieldAfter = afterObj.getClass().getDeclaredField(field.getName());
            fieldAfter.setAccessible(true);

            String before = getFieldValue(field, beforeObj);
            changeMessage = getFieldValue(fieldAfter, afterObj);

            changed = Objects.equals(before, changeMessage);

            if (changed){

                if (nvTrack != null){
                    changeMessage = nvTrack.value();
                }

                this.objectClass = beforeObj.getClass().getName();
                this.objectId = beforeObj.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
            }else{
                changeMessage = null;
            }

        }catch (Exception ex){ex.printStackTrace();}
    }

    void setValue(String fieldName, TrackedEntity beforeObj, TrackedEntity parentObject){

        try{
            Field f = beforeObj.getClass().getField(fieldName);
            NoValueTrack nvTrack = f.getAnnotation(NoValueTrack.class);
            if (nvTrack != null){
                changed = true;
                changeMessage = nvTrack.value();
                this.objectClass = beforeObj.getClass().getName();
                this.objectId = beforeObj.getId();
                this.parentId = parentObject == null ? objectId : parentObject.getId();
                this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
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
