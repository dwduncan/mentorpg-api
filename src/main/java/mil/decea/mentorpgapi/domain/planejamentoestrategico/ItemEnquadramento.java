package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "itensenquadramentos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ItemEnquadramento extends SequenceIdEntity implements Comparable<ItemEnquadramento>{

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TipoEnquadramento enquadramento;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ConfiguracaoTipoEnquadramento configuracaoEnquadramento;
    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String nome;

    protected int ordem;

    private boolean ativo = true;
    public ItemEnquadramento(ConfiguracaoTipoEnquadramento configuracaoEnquadramento){
        this.configuracaoEnquadramento = configuracaoEnquadramento;
        enquadramento = configuracaoEnquadramento.getTipoEnquadramento();
        setOrdem(-1);
    }
    public TipoEnquadramento getEnquadramento() {
        return enquadramento;
    }

    public void setEnquadramento(TipoEnquadramento enquadramento) {
        this.enquadramento = enquadramento;
    }

    @Override
    public int compareTo(ItemEnquadramento o) {
        int comp = getOrdem() - o.ordem;

        if (comp == 0){
            comp = enquadramento.ordinal() - o.enquadramento.ordinal();
        }
        return comp;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla(){
        return enquadramento.getPrefixo() + (getOrdem()+1);
    }

    public String getSiglaENome(){
        return getSigla() + " " + nome;
    }

    public ConfiguracaoTipoEnquadramento getConfiguracaoEnquadramento() {
        return configuracaoEnquadramento;
    }

    public void setConfiguracaoEnquadramento(ConfiguracaoTipoEnquadramento configuracaoEnquadramento) {
        this.configuracaoEnquadramento = configuracaoEnquadramento;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String getEntityDescriptor() {
        return nome;
    }

    @Override
    public TrackedEntity getParentObject() {
        return configuracaoEnquadramento;
    }
}
