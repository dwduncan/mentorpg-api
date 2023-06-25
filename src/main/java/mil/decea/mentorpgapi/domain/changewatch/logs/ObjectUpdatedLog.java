package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.io.Serializable;
import java.util.Objects;

@SuppressWarnings("unchecked")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectUpdatedLog implements FieldChangedWatcher, Serializable {

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

    public ObjectUpdatedLog(TrackedEntity object, TrackedEntity parentObject, String _previousValue, boolean _neverExpires){
        neverExpires = _neverExpires;
        changed = true;
        previousValue = _previousValue;
        this.objectClass = object.getClass().getName();
        this.objectId = object.getId();
        this.parentId = parentObject == null ? objectId : parentObject.getId();
        this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
    }

    /**
     * Always set the object id like setObjectId does, however it will set the parent id only
     * if objectClass and parentClass are equal.
     * @param id
     */
    public void setObjectAndParentIdIfEquals(Long id){
        objectId = id;
        if (Objects.equals(parentClass, objectClass)){
            parentId = id;
        }
    }

}
