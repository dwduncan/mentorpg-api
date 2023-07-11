package mil.decea.mentorpgapi.domain.planejamentoestrategico;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.academic.TeseDissertacao;

import java.util.Objects;

@Table(name = "tesesrelacionadas", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TesesRelacionadas implements Comparable<TesesRelacionadas> {

    @EmbeddedId
    private TeseRelacionadaId teseRelacionadaId;

    @MapsId("idPropostaOperacional")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private PropostaOperacional propostaOperacional;

    @MapsId("idTese")
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private TeseDissertacao teseRelacionada;

    @NotNull
    @Column(columnDefinition = "TEXT", nullable = false)
    private String relacaoComATese = "";

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private VinculoTeses forcaDoVinculo;

    public TesesRelacionadas(PropostaOperacional propostaOperacional, TeseDissertacao teseRelacionada) {
        setTeseRelacionada(teseRelacionada);
        setPropostaOperacional(propostaOperacional);
    }

    public TeseDissertacao getTeseRelacionada() {
        return teseRelacionada;
    }

    public void setTeseRelacionada(TeseDissertacao teseRelacionada) {
        this.teseRelacionada = teseRelacionada;
        if (teseRelacionadaId == null) teseRelacionadaId = new TeseRelacionadaId();
        teseRelacionadaId.setTese(teseRelacionada);
    }

    public PropostaOperacional getPropostaOperacional() {
        return propostaOperacional;
    }

    public void setPropostaOperacional(PropostaOperacional propostaOperacional) {
        this.propostaOperacional = propostaOperacional;
        if (teseRelacionadaId == null) teseRelacionadaId = new TeseRelacionadaId();
        teseRelacionadaId.setProposta(propostaOperacional);
    }

    public String getRelacaoComATese() {
        return relacaoComATese;
    }

    public void setRelacaoComATese(String relacaoComATese) {
        if (relacaoComATese == null) relacaoComATese = "";
        this.relacaoComATese = relacaoComATese;
    }

    public Long getIdTeseDissertacao(){
        return teseRelacionada.getId();
    }

    public String getTituloTese(){
        return teseRelacionada.getTitulo();
    }
    @Override
    public int compareTo(TesesRelacionadas o) {

        if (o.forcaDoVinculo != forcaDoVinculo){
            return forcaDoVinculo.ordinal() - o.forcaDoVinculo.ordinal();
        }

        return getTituloTese().compareToIgnoreCase(o.getTituloTese());
    }
    public TeseRelacionadaId getId() {
        return teseRelacionadaId;
    }
    public void setId(TeseRelacionadaId id) {
        teseRelacionadaId = id;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TesesRelacionadas)) return false;
        if (!super.equals(o)) return false;
        TesesRelacionadas that = (TesesRelacionadas) o;
        return teseRelacionadaId.equals(that.teseRelacionadaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teseRelacionadaId);
    }

    @Override
    public TesesRelacionadas clone() {
        TesesRelacionadas clone = null;
        try {
            clone = (TesesRelacionadas) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        clone.propostaOperacional = null;
        clone.setTeseRelacionada(getTeseRelacionada());
        return clone;
    }
}
