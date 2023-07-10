package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "alunos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Aluno extends Pesquisador{

    @Override
    public String getEntityDescriptor() {
        return "Aluno " + user.getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return user;
    }

}
