package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.administrativo.OrgaoInstitucional;

@Table(name = "orgaodemandadopesquisa", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrgaoDemandadoPesquisa extends SequenceIdEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private OrgaoInstitucional orgaoInstitucional;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private PropostaOperacional propostaOperacional;

    protected int ordem;

    public OrgaoDemandadoPesquisa(OrgaoInstitucional orgaoInstitucional, PropostaOperacional propostaOperacional,int indice) {
        if (indice < 0) throw new ArrayIndexOutOfBoundsException("O índice selecionado não é válido");
        this.orgaoInstitucional = orgaoInstitucional;
        this.propostaOperacional = propostaOperacional;
        setOrdem(indice);
    }

    public void setOrdem(int ordem) {
        if (ordem < 0) throw new ArrayIndexOutOfBoundsException("O índice selecionado não é válido");
        setOrdem(ordem);
    }

    public OrgaoInstitucional getOrgaoInstitucional() {
        return orgaoInstitucional;
    }

    public void setOrgaoInstitucional(OrgaoInstitucional orgaoInstitucional) {
        this.orgaoInstitucional = orgaoInstitucional;
        setId(orgaoInstitucional.getId());
    }

    public PropostaOperacional getPropostaOperacional() {
        return propostaOperacional;
    }

    public void setPropostaOperacional(PropostaOperacional propostaOperacional) {
        this.propostaOperacional = propostaOperacional;
    }

    @Override
    public OrgaoDemandadoPesquisa clone() {
        OrgaoDemandadoPesquisa clone = (OrgaoDemandadoPesquisa) super.clone();
        clone.propostaOperacional = null;
        return clone;
    }

    @Override
    public String getEntityDescriptor() {
        return orgaoInstitucional.getEntityDescriptor();
    }

    @Override
    public TrackedEntity getParentObject() {
        return propostaOperacional.getParentObject();
    }
}
