package mil.decea.mentorpgapi.domain.agenda;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class EventoBasico extends SequenceIdEntity implements Comparable<EventoBasico> {


    @Column(columnDefinition = "DATE")
    private LocalDate diaInicio;

    @Column(columnDefinition = "DATE")
    private LocalDate diaTermino;

    @Column(columnDefinition = "TIME")
    private LocalTime horaInicio;

    @Column(columnDefinition = "TIME")
    private LocalTime horaTermino;

    @Column
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(columnDefinition = "TEXT")
    private String definicoesEstilo;

    @Column
    @Enumerated(EnumType.STRING)
    private Envolvidos envolvidos = Envolvidos.TODOS;

    private boolean oDiaTodo;

    @Transient
    private boolean editavel = false;

    public void  setValoresBasicos(LocalDate diaInicio, LocalDate diaTermino, LocalTime horaInicio, LocalTime horaTermino) {
        this.diaInicio = diaInicio;
        this.diaTermino = diaTermino;
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
    }

    public LocalDate getDiaInicio() {
        return diaInicio;
    }

    public LocalDateTime getDiaHoraInicio() {
        return LocalDateTime.of(diaInicio,horaInicio);
    }

    public void setDiaInicio(LocalDate diaInicio) {
        this.diaInicio = diaInicio;
        if (diaInicio != null && diaTermino != null && diaInicio.isAfter(diaTermino)){
            diaTermino = diaInicio;
        }
    }

    public LocalDateTime getDiaHoraTermino() {
        return LocalDateTime.of(diaTermino,horaTermino);
    }

    public LocalDate getDiaTermino() {
        return diaTermino;
    }

    public void setDiaTermino(LocalDate diaTermino) {
        this.diaTermino = diaTermino;
        if (diaInicio != null && diaTermino != null && diaTermino.isBefore(diaInicio)){
            diaInicio = diaTermino;
        }
    }

    public LocalTime getHoraInicio() {
        if (isODiaTodo()) return LocalTime.of(0,0);
        if (horaInicio == null) horaInicio = LocalTime.now().withMinute(0).plusHours(1);
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
        if (horaInicio != null && horaTermino != null && horaInicio.isAfter(horaTermino)){
            horaTermino = horaInicio.plusHours(1);
        }
    }

    public LocalTime getHoraTermino() {
        if (isODiaTodo()) return LocalTime.of(23,59,59);
        if (horaTermino == null) horaTermino = LocalTime.now().withMinute(0).plusHours(2);
        return horaTermino;
    }

    public void setHoraTermino(LocalTime horaTermino) {
        this.horaTermino = horaTermino;
        if (horaInicio != null && horaTermino != null && horaTermino.isBefore(horaInicio)){
            horaInicio = horaTermino.minusHours(1);
        }
    }


    @Override
    public int compareTo(EventoBasico o) {
        return LocalDateTime.of(getDiaInicio(),getHoraInicio()).compareTo(LocalDateTime.of(o.getDiaInicio(),o.getHoraInicio()));
    }

    @Override
    public String toString() {
        return "";
    }


    @Override
    public String getEntityDescriptor() {
        return "Evento " + getTitulo();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
