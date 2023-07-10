package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

@Table(name = "candidatos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Candidato extends Pesquisador{



    @Override
    public String getEntityDescriptor() {
        return "Candidato " + user.getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return user;
    }

}
