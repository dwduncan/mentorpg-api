package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.user.ForcaSingular;

@Table(name = "orgaoinstitucional", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrgaoInstitucional extends SequenceIdEntity implements Comparable<OrgaoInstitucional> {

    @Enumerated(EnumType.ORDINAL)
    private ForcaSingular forcaSingular = ForcaSingular.FAB;

    @Enumerated(EnumType.STRING)
    private CategoriaOrgao categoriaOrgao = CategoriaOrgao.NAODEF;

    @ManyToOne(fetch = FetchType.LAZY)
    private  OrgaoInstitucional orgaoImediatamenteSuperior;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private  String sigla;
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String nome;

    private boolean ativo = true;

    private long subcategorias;
    private boolean sediadoNoCampus;


    public void setCategoriaOrgao(CategoriaOrgao nivelInstitucional) {
        if (nivelInstitucional == null) nivelInstitucional = CategoriaOrgao.NAODEF;
        this.categoriaOrgao = nivelInstitucional;
    }

    public void setOrgaoImediatamenteSuperior(OrgaoInstitucional orgaoImediatamenteSuperior) {
        this.orgaoImediatamenteSuperior = orgaoImediatamenteSuperior;
    }

    public String getSiglaOrgaoSuperior(){
        return orgaoImediatamenteSuperior != null ? orgaoImediatamenteSuperior.getSigla() : "";
    }


    @Override
    public int compareTo(OrgaoInstitucional o) {
        return getSigla().compareToIgnoreCase(o.getSigla());
    }

    @Override
    public String getEntityDescriptor() {
        return nome;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
