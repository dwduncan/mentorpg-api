package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.daoservices.minio.ExternalData;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ExternalDataEntity<T extends ExternalDataEntity<T, Z>, Z extends IdentifiedRecord> extends AbstractExternalData<T,Z> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    boolean ativo = true;

}
