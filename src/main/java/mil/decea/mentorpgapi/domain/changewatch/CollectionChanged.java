package mil.decea.mentorpgapi.domain.changewatch;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Objects;

@SuppressWarnings("unchecked")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionChanged implements FieldChangedWatcher {

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

    public CollectionChanged(TrackedEntity<?> beforeObj, TrackedEntity<?> afterObj, TrackedEntity<?> parentObject){
        setValue(beforeObj,afterObj, parentObject);
    }

    void setValue(TrackedEntity<?> beforeObj, TrackedEntity<?> afterObj, TrackedEntity<?> parentObject){

        if ((beforeObj == null && afterObj == null) || (beforeObj != null && afterObj != null)) return;

        if (beforeObj != null){
            //removed case
            previousValueOrMessage = "removeu " + beforeObj.getEntityDescriptor();
            changed = true;
        }else{
            //inserted case
            previousValueOrMessage = "inseriu " + afterObj.getEntityDescriptor();
            changed = true;
        }

        if (changed){

            this.objectClass = beforeObj.getClass().getName();
            this.objectId = beforeObj.getId();
            this.parentId = parentObject.getId();
            this.parentClass = parentObject.getClass().getName();
            this.fieldName = "java.util.Collection";

        }else{
            previousValueOrMessage = null;
        }

    }

}