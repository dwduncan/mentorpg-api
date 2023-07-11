package mil.decea.mentorpgapi.domain.agenda;

import fab.mentorpg.core.pessoal.Usuario;
import jakarta.persistence.*;

@Entity(name = "mentorpg_evtadm")
@Table(schema = "mentorpg", name = "agenda_evtsadms")
public class EventoAdministrativo extends AbstractGeradorEventos<EventoAdministrativo> {

    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario responsavel;

    @Column
    private String categoria;

    @Override
    public EventoAdministrativo getGeradorEventos() {return this;}

    public EventoAdministrativo() {
    }

    public EventoAdministrativo(SubCategoriaEventoCalendario subCategoriaEvento) {
        super(subCategoriaEvento);
    }

    @Override
    public boolean isCadastravelPeloUsuario() {
        return true;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario usuario) {
        this.responsavel = usuario;
    }

}
