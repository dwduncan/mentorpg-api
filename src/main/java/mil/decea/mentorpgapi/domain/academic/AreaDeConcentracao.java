package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.user.User;

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
    @ObjectForRecordField
    private User representanteDaArea;

    @Column
    @NotNull(message = "Obrigatório informar a sigla")
    private String sigla = "";
    @Column(columnDefinition = "TEXT")
    private String definicao;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @ObjectForRecordField
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

}
