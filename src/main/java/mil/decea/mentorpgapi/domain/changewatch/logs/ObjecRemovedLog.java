package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjecRemovedLog implements FieldChangedWatcher, Serializable {

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

    public ObjecRemovedLog(TrackedEntity object, TrackedEntity parentObject, boolean removedFromDB){
        neverExpires = true;
        changed = true;
        String rdb = removedFromDB ? "Objeto totalmente removido da base de dados: " : "Objeto removido: ";
        previousValue = rdb + object.getEntityDescriptor();
        this.objectClass = object.getClass().getName();
        this.objectId = object.getId();
        this.parentId = parentObject == null ? objectId : parentObject.getId();
        this.parentClass = parentObject == null ? objectClass : parentObject.getClass().getName();
    }

}
