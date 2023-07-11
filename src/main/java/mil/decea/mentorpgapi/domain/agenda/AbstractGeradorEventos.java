package mil.decea.mentorpgapi.domain.agenda;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class AbstractGeradorEventos<T extends AbstractGeradorEventos<T>> extends EventoBasico implements Periodicidade, GeradorEventos<T> {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.ORDINAL)
    private CICLO ciclo = CICLO.UNICO;
    @Enumerated(EnumType.STRING)
    private SubCategoriaEventoCalendario subCategoriaEvento;
    private int diasSemana;

    @Transient
    protected List<EventoGerado<T>> eventosGerados;
    @Transient
    private LocalDate dataLimiteProgramacao;

    public abstract boolean isCadastravelPeloUsuario();

    public AbstractGeradorEventos(SubCategoriaEventoCalendario subCategoriaEvento) {
        this.subCategoriaEvento = subCategoriaEvento;
        this.setTitulo(subCategoriaEvento.getSubCategoria());
    }

    public abstract T getGeradorEventos();

    protected void gerarEventos(){
        eventosGerados = new ArrayList<>();
        switch (ciclo){
            case UNICO: eventosGerados.add(new EventoGerado<>(getGeradorEventos()));break;
            case DIARIO: gerarEventosDiarios();break;
            case SEMANAL: gerarEventosSemanais();break;
            case MENSAL: gerarEventosMensais();break;
            case ANUAL: gerarEventosAnuais();break;
        }
    }

    private void gerarEventosDiarios(){
        eventosGerados = new ArrayList<>();
        LocalDate ref = getDiaInicio();
        while (!ref.isAfter(getDiaTermino())){
            EventoGerado<T> evt = new EventoGerado<>(getGeradorEventos(),ref);
            if (!eventosGerados.contains(evt)) eventosGerados.add(evt);
            ref = ref.plusDays(1);
        }
    }

    private void gerarEventosSemanais(){
        eventosGerados = new ArrayList<>();
        LocalDate ref = getDiaInicio();
        while (!ref.isAfter(getDiaTermino())){
            if ((diasSemana & Periodicidade.mapaDiasSemana.get(ref.getDayOfWeek())) != 0) {
                EventoGerado<T> evt = new EventoGerado<>(getGeradorEventos(),ref);
                if (!eventosGerados.contains(evt)) eventosGerados.add(evt);
            }
            ref = ref.plusDays(1);
        }
    }

    private void gerarEventosMensais(){
        eventosGerados = new ArrayList<>();
        LocalDate ref = getDiaInicio();
        while (!ref.isAfter(getDiaTermino())){
            EventoGerado<T> evt = new EventoGerado<>(getGeradorEventos(),ref);
            if (!eventosGerados.contains(evt)) eventosGerados.add(evt);
            ref = ref.plusMonths(1);
        }
    }

    private void gerarEventosAnuais(){
        eventosGerados = new ArrayList<>();
        LocalDate ref = getDiaInicio();
        while (!ref.isAfter(getDiaTermino())){
            eventosGerados.add(new EventoGerado<>(getGeradorEventos(),ref));
            ref = ref.plusYears(1);
        }
    }

    @Override
    public List<EventoGerado<T>> getEventosGerados() {
        if (eventosGerados == null) gerarEventos();
        return eventosGerados;
    }


    public List<Integer> getDiasSelecionadosPorValorUnario(){
        return mapaDiasSemana.values().stream().filter(v->(v & diasSemana) != 0).collect(Collectors.toList());
    }

    public void setDiasSelecionadosPorValorUnario(List<? extends Number> lista){
        diasSemana = lista.stream().filter(Objects::nonNull).mapToInt(Number::intValue).sum();
    }

}
