package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.EntityDTOAdapter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import mil.decea.mentorpgapi.domain.user.User;

import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
@Table(name = "programaposgraduacao", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProgramaPosGraduacao extends SequenceIdEntity {

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String nome = "";

    @ManyToOne
    private User coordenador;

    @Column(columnDefinition = "TEXT")
    private String definicao = "";


    @NotNull(message = "Obrigatório informar a sigla")
    @Column(columnDefinition = "TEXT")
    private String sigla = "";

    @Column(columnDefinition = "TEXT")
    private String fatoresCondicionantes;

    @OrderBy
    @OneToMany(mappedBy = "programa")
    @CollectionForRecordField(elementsOfType = AreaDeConcentracao.class)
    private List<AreaDeConcentracao> areasConcentracao = new ArrayList<>();

    public void setSigla(String sigla) {
        if (sigla == null) sigla = "";
        else sigla = sigla.toUpperCase();
        this.sigla = sigla;
    }

    public List<AreaDeConcentracao> getAreasConcentracao() {
        return areasConcentracao;
    }

    public void setAreasConcentracao(List<AreaDeConcentracao> areasConcentracao) {
        this.areasConcentracao = areasConcentracao;
    }

    public AreaDeConcentracao getAreaConcentracao(String sigla){
        return getAreasConcentracao().stream().filter(a->a.getSigla().equalsIgnoreCase(sigla)).findAny().orElse(null);
    }

    public String getNomeCoordenador(){
        return getCoordenador() != null ? getCoordenador().getNomeQualificado() : "";
    }

    @Override
    public String getEntityDescriptor() {
        return nome;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }


}

