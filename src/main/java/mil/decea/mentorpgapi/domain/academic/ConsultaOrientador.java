package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.administrativo.ControladorOcorrencias;
import mil.decea.mentorpgapi.domain.administrativo.DespachoConsultaOrientacao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Table(name = "consultasorientadores", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ConsultaOrientador extends SequenceIdEntity implements ControladorOcorrencias<DespachoConsultaOrientacao>, Comparable<ConsultaOrientador> {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Professor orientador;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidato candidato;

    @ManyToOne
    private ProgramaPosGraduacao programaEscolhido;

    @Enumerated(EnumType.STRING)
    private StatusConsultaOrientador statusConsulta = StatusConsultaOrientador.NAO_CONSULTADO;

    @Column(columnDefinition = "text")
    private String observacoes;

    private int prioridade = 1;

    protected int ordem;

    @OneToMany(mappedBy = "consultaOrientador")
    private Set<DespachoConsultaOrientacao> despachos;

    public Professor getOrientador() {
        return orientador;
    }

    public void setOrientador(Professor professor) {
        this.orientador = professor;
        if (professor != null && professor.getProgramasCredenciado().size() == 1){
            programaEscolhido = new ArrayList<>(professor.getProgramasCredenciado()).get(0);
        }
    }

    public Set<DespachoConsultaOrientacao> getDespachos() {
        if (despachos == null) despachos = new HashSet<>();
        return despachos;
    }

    public void addOrUpdateDespacho(DespachoConsultaOrientacao despacho){
        if (despacho.getId() != null){
            getDespachos().remove(despacho);
        }
        despacho.setConsultaOrientador(this);
        getDespachos().add(despacho);
    }


    @Override
    public int compareTo(ConsultaOrientador o) {
        return prioridade - o.prioridade;
    }

    @Override
    public String toString() {
        return getOrientador().getUser().getNomeQualificado();
    }

    public DespachoConsultaOrientacao getDespachoPorId(Long id){
        return id != null ? getDespachos().stream().filter(d->d.getId().equals(id)).findAny().orElse(null) : null;
    }

    @Override
    public String getEntityDescriptor() {
        return "Consulta orientador id " +getId();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
