package mil.decea.mentorpgapi.domain.changewatch;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;

public interface TrackedElementCollection<T,Z extends IdentifiedRecord> extends TrackedEntity<Z> {

    void copyFields(T previousEntity);
}
