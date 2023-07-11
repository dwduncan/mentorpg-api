package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import mil.decea.mentorpgapi.domain.academic.TeseDissertacao;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TeseRelacionadaId implements Serializable {

    @Column(nullable = false)
    private Long idPropostaOperacional;
    @Column(nullable = false)
    private Long idTese;

    public TeseRelacionadaId() {
    }

    public TeseRelacionadaId(Long idPropostaOperacional, Long idTese) {
        this.idPropostaOperacional = idPropostaOperacional;
        this.idTese = idTese;
    }

    public TeseRelacionadaId(PropostaOperacional propostaOperacional, TeseDissertacao tese) {
        setProposta(propostaOperacional);
        setTese(tese);
    }

    public void setProposta(PropostaOperacional propostaOperacional){
        idPropostaOperacional = propostaOperacional.getId();
    }

    public void setTese(TeseDissertacao tese){
        idTese = tese.getId();
    }


    public Long getIdPropostaOperacional() {
        return idPropostaOperacional;
    }

    public void setIdPropostaOperacional(Long idPropostaOperacional) {
        this.idPropostaOperacional = idPropostaOperacional;
    }

    public Long getIdTese() {
        return idTese;
    }

    public void setIdTese(Long idTese) {
        this.idTese = idTese;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeseRelacionadaId)) return false;
        TeseRelacionadaId that = (TeseRelacionadaId) o;
        return idPropostaOperacional.equals(that.idPropostaOperacional) && idTese.equals(that.idTese);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPropostaOperacional, idTese);
    }
}
