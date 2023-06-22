package mil.decea.mentorpgapi.domain.changewatch;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@SuppressWarnings("unchecked")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FieldChanged<O> implements FieldChangedWatcher {

    @NotNull
    protected Long objectId;
    @NotNull
    protected String objectClass;
    @NotNull
    protected String fieldName;
    @NotNull
    protected String fieldType;

    protected String before;

    @NotNull
    protected String afterOrMessage;

    @NotNull
    protected Long objectOwnerId;
    @NotNull
    protected String objectOwnerClass;


    public FieldChanged(String _fieldName, ChangeWatcher<?,?> beforeObj, IdentifiedRecord afterObj){
        setValues(getFieldByName(beforeObj, _fieldName),beforeObj,afterObj);
    }

    public FieldChanged(Field field, ChangeWatcher<?,?> beforeObj, IdentifiedRecord afterObj){
        setValues(field,beforeObj,afterObj);
    }

    void setValues(Field field, ChangeWatcher<?,?> beforeObj, IdentifiedRecord afterObj){
        this.objectClass = beforeObj.getClass().getName();
        this.objectId = beforeObj.getId();
        this.objectOwnerId = beforeObj.getId();
        this.objectOwnerClass = this.objectClass;
        this.fieldName = field.getName();
        try {
            this.fieldType = field.getType().getName();
            field.setAccessible(true);
            O prevValue = (O) field.get(beforeObj);
            O newValue =  (O) afterObj.getClass().getDeclaredMethod(fieldName).invoke(afterObj);
            field.set(beforeObj, newValue);
            before = prevValue != null ? prevValue.toString() : "";
            afterOrMessage = newValue != null ? newValue.toString() : "";

        }catch (Exception ex){ex.printStackTrace();}
    }

    Field getFieldByName(ChangeWatcher<?,?> beforeObj, String _fieldName){

        Class<?> actual = beforeObj.getClass();

        while (actual != Object.class){

            try {
                return actual.getDeclaredField(_fieldName);
            } catch (NoSuchFieldException e) {
                actual = actual.getSuperclass();
            }

            if (actual == null) break;
        }

        return null;
    }


}
