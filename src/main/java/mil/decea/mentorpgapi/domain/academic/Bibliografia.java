package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "bibliografias", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Bibliografia extends SequenceIdEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private RelatorioAcademico relatorioAcademico;

    @Column(columnDefinition = "TEXT")
    private String referenciaBibliografica;

    @Column(columnDefinition = "TEXT")
    private String issn;

    @Column(columnDefinition = "TEXT")
    private String doi;

    public String getTextoImpressao(){
        String i = issn != null && !issn.isBlank() ? " ISSN: " + getIssn() : "";
        String d = doi != null && !doi.isBlank() ? " DOI: " + getDoi() : "";
        return getReferenciaBibliografica() + i + d;
    }

    @Override
    public String getEntityDescriptor() {
        return referenciaBibliografica;
    }

    @Override
    public TrackedEntity getParentObject() {
        return relatorioAcademico;
    }
}
