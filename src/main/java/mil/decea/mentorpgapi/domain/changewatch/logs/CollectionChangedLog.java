package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionChangedLog implements FieldChangedWatcher {

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

    public CollectionChangedLog(TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){
        setValue(beforeObj,afterObj, parentObject);
    }

    void setValue(TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){

        if ((beforeObj == null && afterObj == null) || (beforeObj != null && afterObj != null)) return;

        previousValue = null;
        this.parentId = parentObject.getId();
        this.parentClass = parentObject.getClass().getName();
        changed = true;

        if (beforeObj != null){
            //removed case
            previousValue = "removeu " + beforeObj.getEntityDescriptor();
            this.objectClass = beforeObj.getClass().getName();
            this.objectId = beforeObj.getId();
        }else{
            //inserted case
            previousValue = "inseriu " + afterObj.getEntityDescriptor();
            this.objectClass = afterObj.getClass().getName();
            this.objectId = afterObj.getId();
        }

    }

}
