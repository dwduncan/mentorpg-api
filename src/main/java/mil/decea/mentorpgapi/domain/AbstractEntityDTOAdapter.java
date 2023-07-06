package mil.decea.mentorpgapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;

@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractEntityDTOAdapter<E extends TrackedEntity, R extends IdentifiedRecord> implements EntityDTOAdapter<E,R> {

    protected E entity;
    protected R identifiedRecord;
    public AbstractEntityDTOAdapter(E entity, R identifiedRecord) {
        this.entity = entity;
        this.identifiedRecord = identifiedRecord;
    }

    @Override
    public ObjectChangesChecker getChangesAndUpdate() {
        ObjectChangesChecker c = new ObjectChangesChecker(getEntity(), this.getIdentifiedRecord(), getEntity().getParentObject());
        updateEntity();
        return c;
    }

}
