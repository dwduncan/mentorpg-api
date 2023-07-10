package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.util.HashSet;
import java.util.Set;

@Table(name = "professores", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Professor extends Pesquisador{

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "professores_programas",
            joinColumns = { @JoinColumn(name = "prof_id") },
            inverseJoinColumns = { @JoinColumn(name = "prog_id")})
    private Set<ProgramaPosGraduacao> programasCredenciado;

    private boolean exProfessor;
    @Override
    public String getEntityDescriptor() {
        return user.getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return user;
    }

    public Set<ProgramaPosGraduacao> getProgramasCredenciado() {
        if (programasCredenciado == null) programasCredenciado = new HashSet<>();
        return programasCredenciado;
    }

    public String getSiglasProgramasCredenciado(){

        if (getProgramasCredenciado().isEmpty()) return "Não credenciado";

        StringBuilder sigla = new StringBuilder();
        for(ProgramaPosGraduacao p : getProgramasCredenciado()){
            if (!sigla.toString().isBlank()) sigla.append("/");
            sigla.append(p.getSigla());
        }
        return sigla.toString();
    }

}
