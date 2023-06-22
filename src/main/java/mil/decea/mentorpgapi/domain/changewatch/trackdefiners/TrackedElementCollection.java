package mil.decea.mentorpgapi.domain.changewatch.trackdefiners;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;

public interface TrackedElementCollection<T> extends TrackedEntity {
    void copyFields(T previousEntity);
}
