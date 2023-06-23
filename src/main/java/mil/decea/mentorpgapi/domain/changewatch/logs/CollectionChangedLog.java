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

    protected String changeMessage;

    protected boolean neverExpires;

    private boolean changed;

    public CollectionChangedLog(TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){
        setValue(beforeObj,afterObj, parentObject);
    }

    void setValue(TrackedEntity beforeObj, TrackedEntity afterObj, TrackedEntity parentObject){

        if ((beforeObj == null && afterObj == null) || (beforeObj != null && afterObj != null)) return;

        if (beforeObj != null){
            //removed case
            changeMessage = "removeu " + beforeObj.getEntityDescriptor();
            changed = true;
        }else{
            //inserted case
            changeMessage = "inseriu " + afterObj.getEntityDescriptor();
            changed = true;
        }

        if (changed){
            this.objectClass = beforeObj.getClass().getName();
            this.objectId = beforeObj.getId();
            this.parentId = parentObject.getId();
            this.parentClass = parentObject.getClass().getName();

        }else{
            changeMessage = null;
        }

    }

}
