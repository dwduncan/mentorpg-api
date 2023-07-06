package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.RecordFieldName;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

/**
 * Força Aérea Brasileira - (FAB)
 * Instituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * PPGAO-MENTOR
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 24/03/2021
 * Time: 19:53
 */

@Table(name = "areadeconcentracao", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AreaDeConcentracao extends SequenceIdEntity implements Comparable<AreaDeConcentracao> {

    @Column
    private String nome = "";

    @ManyToOne
    private User representanteDaArea;

    @Column
    @NotNull(message = "Obrigatório informar a sigla")
    private String sigla = "";
    @Column(columnDefinition = "TEXT")
    private String definicao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramaPosGraduacao programa;


    public User getRepresentanteDaArea() {
        return representanteDaArea;
    }

    public String getNomeRepresentante(){
        return getRepresentanteDaArea() != null ? getRepresentanteDaArea().getNomeQualificado() : "";
    }


    public void setSigla(String sigla) {
        if (sigla == null) sigla = "";
        else sigla = sigla.toUpperCase();
        this.sigla = sigla;
    }

    @Override
    public int compareTo(AreaDeConcentracao o) {
        return getSigla().compareToIgnoreCase(o.getSigla());
    }

    @Override
    public String getEntityDescriptor() {
        return nome;
    }

    @Override
    public TrackedEntity getParentObject() {
        return programa;
    }

    @NotForRecordField
    public ObjectChangesChecker onValuesUpdated(IdentifiedRecord incomingData) {

        AreaDeConcentracaoRecord rec = (AreaDeConcentracaoRecord) incomingData;

        ObjectChangesChecker changes = new ObjectChangesChecker(this, rec, programa);


        //this.setRepresentanteDaArea(rec.representanteDaArea());
        this.setSigla(rec.sigla());
        this.setNome(rec.nome());
        this.setPrograma(rec.programa());
        this.setDefinicao(rec.definicao());
        this.setId(rec.id());
        this.setLastUpdate(DateTimeAPIHandler.converterStringDate(rec.lastUpdate()));
        this.setAtivo(rec.ativo());

        return changes;
    }
}
