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


@SuppressWarnings("unused")
@Table(name = "linhadepesquisa", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class LinhaDePesquisa extends SequenceIdEntity  implements Comparable<LinhaDePesquisa>{

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private AreaDePesquisa areaDePesquisa;
    @NotNull
    @Column(nullable = false)
    private String nome;
    @Column(columnDefinition = "TEXT")
    private String conceito;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public String toString() {
        return nome;
    }


    @Override
    public int compareTo(LinhaDePesquisa o) {
        return getNome().compareToIgnoreCase(o.getNome());
    }

    @Override
    public String getEntityDescriptor() {
        return nome;
    }

    @Override
    public TrackedEntity getParentObject() {
        return areaDePesquisa;
    }
}
