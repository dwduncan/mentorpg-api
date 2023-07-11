package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.util.Objects;


public class EnquadramentoSelecionadoProxy extends AbstractEnquadramentoSelecionado {

    private PropostaOperacional propostaOperacional;
    final EnquadramentoSelecionado enquadramento;
    protected ItemEnquadramento itemEnquadramento;

    public EnquadramentoSelecionadoProxy(EnquadramentoSelecionado enquadramento){
        this.enquadramento = enquadramento;
        setValues(enquadramento);
    }

    void setValues(EnquadramentoSelecionado enquadramento){
        super.setEnquadramento(enquadramento.getEnquadramento());
        super.setOrdem(enquadramento.getOrdem());
        setItemEnquadramento(enquadramento.getItemEnquadramento());
        super.setJustificativa(enquadramento.getJustificativa());
        setPropostaOperacional(enquadramento.getPropostaOperacional());
    }

    @Override
    public void setEnquadramento(TipoEnquadramento enquadramento) {
        super.setEnquadramento(enquadramento);
        this.enquadramento.setEnquadramento(enquadramento);
    }

    @Override
    public void setItemEnquadramento(ItemEnquadramento itemEnquadramento) {
        this.itemEnquadramento = itemEnquadramento;
        enquadramento.setItemEnquadramento(itemEnquadramento);
    }

    @Override
    public void setJustificativa(String justificativa) {
        super.setJustificativa(justificativa);
        enquadramento.setJustificativa(justificativa);
    }

    @Override
    public void setOrdem(int ordem) {
        super.setOrdem(ordem);
        enquadramento.setOrdem(ordem) ;
    }

    @Override
    public void setPropostaOperacional(PropostaOperacional propostaOperacional) {
        this.propostaOperacional = propostaOperacional;
        enquadramento.setPropostaOperacional(propostaOperacional);
    }

    @Override
    public void setId(Long id) {
        super.setId(id);
        enquadramento.setId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnquadramentoSelecionado)) return false;
        if (!super.equals(o)) return false;
        EnquadramentoSelecionadoProxy that = (EnquadramentoSelecionadoProxy) o;
        return getItemEnquadramento().equals(that.getItemEnquadramento());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getItemEnquadramento());
    }

    public EnquadramentoSelecionado getEnquadramentoSelecionado(){
        return enquadramento;
    }

    @Override
    public PropostaOperacional getPropostaOperacional() {
        return propostaOperacional;
    }

    @Override
    public ItemEnquadramento getItemEnquadramento() {
        return itemEnquadramento;
    }

    @Override
    public String getEntityDescriptor() {
        return null;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
