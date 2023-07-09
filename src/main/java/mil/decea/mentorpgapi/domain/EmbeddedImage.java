package mil.decea.mentorpgapi.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedByStringComparison;

@Getter
@Setter
@NoArgsConstructor
@TrackedByStringComparison(recordFieldToCompare = "nomeArquivo")
@Embeddable
public class EmbeddedImage extends EmbeddedExternalData {

    @Override
    @Transient
    public TrackedEntity getParentObject() {
        return null;
    }

}
