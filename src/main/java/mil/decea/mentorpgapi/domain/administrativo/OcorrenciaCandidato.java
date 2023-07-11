package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.academic.Candidato;


@Table(name = "ocorrenciascandidatos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OcorrenciaCandidato extends Despacho{

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Candidato candidato;

    public void deleteAnexos(){
        getAnexos().clear();
    }
}
