package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "itensenquadramento", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConfiguracaoTipoEnquadramento extends SequenceIdEntity {

    @Id
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String conceito;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private TipoEnquadramento tipoEnquadramento;

    @OrderBy(value = "ordem")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "configuracaoEnquadramento")
    private final List<ItemEnquadramento> itensEnquadramento = new ArrayList<>();

    private int minimoSelecionavel;
    private int maximoSelecionavel = 5;

    private boolean ativado = true;

    public ConfiguracaoTipoEnquadramento(TipoEnquadramento tipoEnquadramento) {
        this.tipoEnquadramento = tipoEnquadramento;
        this.id = tipoEnquadramento.ordinal()+1L;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {

    }

    public String getConceito() {
        return conceito;
    }

    public void setConceito(String conceito) {
        this.conceito = conceito;
    }

    public TipoEnquadramento getTipoEnquadramento() {
        return tipoEnquadramento;
    }

    public boolean isAtivado() {
        return ativado;
    }

    public void setAtivado(boolean desativado) {
        this.ativado = desativado;
    }

    public int getMinimoSelecionavel() {
        return minimoSelecionavel;
    }

    public void setMinimoSelecionavel(int minimoSelecionavel) {
        this.minimoSelecionavel = minimoSelecionavel;
        if (minimoSelecionavel > maximoSelecionavel){
            maximoSelecionavel = minimoSelecionavel;
        }
    }

    public int getMaximoSelecionavel() {
        return maximoSelecionavel;
    }

    public void setMaximoSelecionavel(int maximoSelecionavel) {
        this.maximoSelecionavel = maximoSelecionavel;
        if (maximoSelecionavel < minimoSelecionavel) minimoSelecionavel = maximoSelecionavel;
    }

    public List<ItemEnquadramento> getItensEnquadramento() {
        return itensEnquadramento;
    }

    public List<ItemEnquadramento> getItensAtivosEnquadramento() {
        return itensEnquadramento.stream().filter(ItemEnquadramento::isAtivo).collect(Collectors.toList());
    }


    @Override
    public String getEntityDescriptor() {
        return tipoEnquadramento.getNome();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
