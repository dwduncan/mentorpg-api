package mil.decea.mentorpgapi.domain.agenda;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@SuppressWarnings("unused")
public class EventoGerado<T extends GeradorEventos<T>> extends EventoBasico implements Serializable {


    String titulo;
    LocalDateTime diaHoraInicio;
    LocalDateTime diaHoraTermino;


    private static final long serialVersionUID = 1L;

    private T geradorEventos;

    public EventoGerado() {
    }

    public EventoGerado(T geradorEventos) {
        this.geradorEventos = geradorEventos;
        setMesmosValores(geradorEventos);
    }

    public EventoGerado(T geradorEventos, LocalDate dataReferencia) {
        this.geradorEventos = geradorEventos;
        setMesmosValores(geradorEventos);
        setDiaInicio(dataReferencia);
        setDiaTermino(dataReferencia);
    }

    public void setMesmosValores(GeradorEventos<?> evt){
        setDescricao(evt.getDescricao());
        setTitulo(evt.getTitulo());
        setDiaInicio(evt.getDiaInicio());
        setDiaTermino(evt.getDiaTermino());
        setHoraInicio(evt.getHoraInicio());
        setHoraTermino(evt.getHoraTermino());
        setODiaTodo(evt.isODiaTodo());
        setEnvolvidos(evt.getEnvolvidos());
        if (evt instanceof EventoBasico) setDefinicoesEstilo(((EventoBasico)evt).getDefinicoesEstilo());
    }

    public T getGeradorEventos() {
        return geradorEventos;
    }

    public void setGeradorEventos(T geradorEventos) {
        this.geradorEventos = geradorEventos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventoGerado)) return false;

        EventoGerado<?> that = (EventoGerado<?>) o;
        if (getDiaHoraInicio().compareTo(that.getDiaHoraInicio()) != 0) return false;
        if (getDiaHoraTermino().compareTo(that.getDiaHoraTermino()) != 0) return false;
        if (!Objects.equals(getDescricao(),that.getDescricao())) return false;
        return Objects.equals(getTitulo(),that.getTitulo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDiaHoraInicio(), getDiaHoraTermino(),getTitulo(),getDescricao());
    }
}
