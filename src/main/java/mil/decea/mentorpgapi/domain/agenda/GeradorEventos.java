package mil.decea.mentorpgapi.domain.agenda;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface GeradorEventos<T extends GeradorEventos<T>> {

    int CATEGORIA_PESSOAL = 1;
    int CATEGORIA_ACADEMICO = 2;
    int CATEGORIA_ADM_ORGANICA = 4;
    int CATEGORIA_ADM_EXTERNA = 8;
    int CATEGORIA_AULA = 16;

    List<EventoGerado<T>> getEventosGerados();
    int getDiasSemana();
    SubCategoriaEventoCalendario getSubCategoriaEvento();
    LocalDate getDiaInicio();
    LocalDate getDiaTermino();
    LocalTime getHoraInicio();
    LocalTime getHoraTermino();
    String getTitulo();
    String getDescricao();
    Envolvidos getEnvolvidos();
    boolean isODiaTodo();
    boolean isEditavel();
    CICLO getCiclo();

    void setDiasSemana(int diasSemana);
    void setSubCategoriaEvento(SubCategoriaEventoCalendario subCategoriaEvento);
    void setDiaInicio(LocalDate diaInicio);
    void setDiaTermino(LocalDate diaTermino);
    void setHoraInicio(LocalTime horaInicio);
    void setHoraTermino(LocalTime horaTermino);
    void setTitulo(String titulo);
    void setDescricao(String descricao);
    void setEnvolvidos(Envolvidos envolvidos);
    void setODiaTodo(boolean odiaTodo);
    void setEditavel(boolean editavel);
    void setCiclo(CICLO ciclo);

    static boolean isEventoAdministrativo(AbstractGeradorEventos<?> evento){
        return evento.getSubCategoriaEvento().isDaCategoria(GeradorEventos.CATEGORIA_ADM_EXTERNA+ GeradorEventos.CATEGORIA_ADM_ORGANICA+ GeradorEventos.CATEGORIA_ACADEMICO);
    }

    static boolean isEventoPessoal(AbstractGeradorEventos<?> evento){
        return evento.getSubCategoriaEvento().isDaCategoria(GeradorEventos.CATEGORIA_PESSOAL);
    }

    static String getNomeGeradorEventos(int codigo){
        String desc = null;

        if ((codigo & CATEGORIA_PESSOAL) != 0){
            desc = desc == null ? "AFASTAMENTOS" : ", AFASTAMENTOS";
        }

        if ((codigo & CATEGORIA_ACADEMICO) != 0){
            desc = desc == null ? "ACADÊMICO" : ", ACADÊMICO";
        }

        if ((codigo & CATEGORIA_ADM_ORGANICA) != 0){
            desc = desc == null ? "ORGÂNICO" : ", ORGÂNICO";
        }

        if ((codigo & CATEGORIA_ADM_EXTERNA) != 0){
            desc = desc == null ? "DEMANDA EXTERNA" : ", DEMANDA EXTERNA";
        }

        if ((codigo & CATEGORIA_AULA) != 0){
            desc = desc == null ? "DISCIPLINA" : ", DISCIPLINA";
        }

        return desc;
    }


}
