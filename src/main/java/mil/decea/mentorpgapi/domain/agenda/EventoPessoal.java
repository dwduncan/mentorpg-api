package mil.decea.mentorpgapi.domain.agenda;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.user.User;

@Table(name = "eventospessoais", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventoPessoal extends AbstractGeradorEventos<EventoPessoal> {

    @ManyToOne(fetch = FetchType.LAZY)
    private User usuario;

    @Column
    private String categoria;

    @Override
    public EventoPessoal getGeradorEventos() {return this;}

    public EventoPessoal(SubCategoriaEventoCalendario subCategoriaEvento) {
        super(subCategoriaEvento);
    }

    @Override
    public boolean isCadastravelPeloUsuario() {
        return true;
    }


}
