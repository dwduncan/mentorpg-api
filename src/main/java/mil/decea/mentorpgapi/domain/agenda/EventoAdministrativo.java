package mil.decea.mentorpgapi.domain.agenda;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.user.User;
@Table(name = "eventosadministrativos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class EventoAdministrativo extends AbstractGeradorEventos<EventoAdministrativo> {

    @ManyToOne(fetch = FetchType.LAZY)
    private User responsavel;

    @Column
    private String categoria;
    @Override
    public EventoAdministrativo getGeradorEventos() {return this;}

    public EventoAdministrativo(SubCategoriaEventoCalendario subCategoriaEvento) {
        super(subCategoriaEvento);
    }
    @Override
    public boolean isCadastravelPeloUsuario() {
        return true;
    }

}
