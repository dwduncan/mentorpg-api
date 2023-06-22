package mil.decea.mentorpgapi.domain.changewatch;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;

import java.io.Serializable;
import java.util.List;

public interface ChangeWatcher<T extends ChangeWatcher<T, Z>, Z extends IdentifiedRecord> extends Serializable {

    T getChangingObject();
    Long getId();
    String getEntityDescriptor();

    List<FieldChanged> updateValues(Z recordDTO);
}
