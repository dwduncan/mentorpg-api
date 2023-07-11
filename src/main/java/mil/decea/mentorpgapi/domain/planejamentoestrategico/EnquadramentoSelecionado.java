package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "enquadramentoselecionado", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EnquadramentoSelecionado extends AbstractEnquadramentoSelecionado {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected PropostaOperacional propostaOperacional;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected ItemEnquadramento itemEnquadramento;


    public EnquadramentoSelecionado(PropostaOperacional propostaOperacional, ItemEnquadramento item){
        this.setItemEnquadramento(item);
        this.enquadramento = item.getEnquadramento();
        this.setPropostaOperacional(propostaOperacional);
    }

    public EnquadramentoSelecionado(PropostaOperacional propostaOperacional, ItemEnquadramento item, int ordem){
        this.setItemEnquadramento(item);
        this.enquadramento = item.getEnquadramento();
        this.setPropostaOperacional(propostaOperacional);
        this.setOrdem(ordem);
    }

    public EnquadramentoSelecionado(EnquadramentoSelecionado outra){
        justificativa = outra.justificativa;
        enquadramento = outra.enquadramento;
        setItemEnquadramento(outra.getItemEnquadramento());
        setOrdem(outra.getOrdem());
    }

    @Override
    public EnquadramentoSelecionado clone() {
        EnquadramentoSelecionado clone = (EnquadramentoSelecionado) super.clone();
        clone.propostaOperacional = null;
        clone.setItemEnquadramento(itemEnquadramento);
        return clone;
    }

    @Override
    public String getEntityDescriptor() {
        return itemEnquadramento.getNome();
    }

    @Override
    public TrackedEntity getParentObject() {
        return propostaOperacional;
    }
}
