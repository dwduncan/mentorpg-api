package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.io.Serializable;

@SuppressWarnings("unchecked")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjecCreatedLog  implements FieldChangedWatcher, Serializable {

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

    public ObjecCreatedLog(TrackedEntity object, TrackedEntity parentObject){
        neverExpires = true;
        changed = true;
        changeMessage = "Objeto inserido: " + object.getEntityDescriptor();
        this.objectClass = object.getClass().getName();
        this.objectId = object.getId();
        this.parentId = parentObject == null ? objectId : parentObject.getId();
        this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
    }

}
