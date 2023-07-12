package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

import java.time.LocalDate;



@Table(name = "matriculasemdisciplinas", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class MatriculaEmDisciplina extends ExternalDataEntity {

    @NotNull(message = "Selecione uma disciplina cadastrada ou solicite à coordenação para cadastrá-la caso não encontre-a")
    @ManyToOne(fetch = FetchType.LAZY)
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    private Aluno aluno;

    @Column(columnDefinition = "DATE")
    private LocalDate inicio;
    @Column(columnDefinition = "DATE")
    private LocalDate termino;

    @Column
    private String conceitoObtido;


    @NotNull
    @Enumerated(EnumType.STRING)
    private SituacaoDisciplina situacaoNaDisciplina = SituacaoDisciplina.PLANEJADO;

    @Override
    public String getEntityDescriptor() {
        return aluno.getUser().getNomeQualificado() + "/" + disciplina.getSigla();
    }

    @Override
    public TrackedEntity getParentObject() {
        return getDisciplina();
    }

    @Override
    @MethodDefaultValue(fieldName = "bucket",defaultValue = "\"comprovantesmatriculas\"")
    public String getBucket() {
        return "comprovantesmatriculas";
    }

    @Override
    @MethodDefaultValue(fieldName = "storageDestinationPath",
            defaultValue = "getDisciplina().getId() +\"/\"+getAluno().getId()+\"/\" + getNomeArquivo()")
    public String getStorageDestinationPath() {
        return getDisciplina().getId() + "/" + getAluno().getId() + "/" + getNomeArquivo();
    }

    @Override
    public MatriculaEmDisciplina getExternalData() {
        return this;
    }

    @NotForRecordField
    @Override
    public String getPreviousStorageDestinationPath() {
        return getDisciplina().getId() + "/" + getAluno().getId() + "/" + getPreviousFileName();
    }

}
