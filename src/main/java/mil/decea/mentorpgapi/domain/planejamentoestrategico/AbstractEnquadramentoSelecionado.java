package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractEnquadramentoSelecionado extends SequenceIdEntity implements Comparable<AbstractEnquadramentoSelecionado> {

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    protected TipoEnquadramento enquadramento;

    @Column(columnDefinition = "TEXT")
    protected String justificativa;
    int ordem;

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public abstract PropostaOperacional getPropostaOperacional();

    public abstract void setPropostaOperacional(PropostaOperacional propostaOperacional);

    public TipoEnquadramento getEnquadramento() {
        return enquadramento;
    }

    public void setEnquadramento(TipoEnquadramento enquadramento) {
        this.enquadramento = enquadramento;
    }

    @Override
    public int compareTo(AbstractEnquadramentoSelecionado o) {
        int comp = getOrdem() - o.getOrdem();

        if (comp == 0) {
            comp = enquadramento.ordinal() - o.enquadramento.ordinal();
        }
        return comp;
    }

    public abstract ItemEnquadramento getItemEnquadramento();

    public abstract void setItemEnquadramento(ItemEnquadramento itemEnquadramento) ;
}
