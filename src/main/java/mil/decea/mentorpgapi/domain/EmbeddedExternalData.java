package mil.decea.mentorpgapi.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedByStringComparison;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@TrackedByStringComparison(recordFieldToCompare = "nomeArquivo")
public class EmbeddedExternalData extends AbstractExternalData {

    @Transient
    protected Long id;
    @Override
    public String toString() {
        return getNomeArquivo();
    }
}
