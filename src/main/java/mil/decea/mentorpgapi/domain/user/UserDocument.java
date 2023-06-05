package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.DocumentType;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;

@Table(name = "userdocs", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserDocument extends ExternalDataEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private DocumentType tipoDocumentacao;
    @Enumerated(EnumType.ORDINAL)
    private StatusDoc statusDocumento = StatusDoc.NOVO;
    @Column(length = 1024)
    private String motivoRecusa;
    private boolean obrigatorio;
    private Long idExigencia;
}
