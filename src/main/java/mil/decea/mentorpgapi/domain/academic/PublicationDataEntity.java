package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.MinioStorage;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class PublicationDataEntity extends AbstractExternalData implements TrackedEntity, MinioStorage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "PUBLICATION_SEQ", sequenceName = "PUBLICATION_SEQ", allocationSize = 1)
    protected Long id;
    boolean ativo = true;

}
