package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.daoservices.minio.ExternalData;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class ExternalDataEntity<T extends ExternalData<T>> extends AbstractExternalData<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    boolean ativo = true;



}
