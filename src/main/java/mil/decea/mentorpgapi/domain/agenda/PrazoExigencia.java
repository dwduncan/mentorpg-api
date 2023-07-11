package mil.decea.mentorpgapi.domain.agenda;

import mil.decea.mentorpgapi.domain.academic.CumprimentoExigencia;
import mil.decea.mentorpgapi.domain.academic.MatriculaEmDisciplina;

public class PrazoExigencia  extends AbstractGeradorEventos<PrazoExigencia> {

    public PrazoExigencia(CumprimentoExigencia cumprimentoExigencia){
        setDescricao("");
        setDiaInicio(cumprimentoExigencia.getPrazoInicio());
        setDiaTermino(cumprimentoExigencia.getPrazoTermino());
        if (cumprimentoExigencia.getDataEstimadaTermino() != null
                && cumprimentoExigencia.getDataEstimadaTermino().isAfter(cumprimentoExigencia.getPrazoTermino())) {
            setDataLimiteProgramacao(cumprimentoExigencia.getDataEstimadaTermino());
            setDiaTermino(cumprimentoExigencia.getPrazoTermino());
        }
        setCiclo(CICLO.UNICO);
        setODiaTodo(true);
        setTitulo(cumprimentoExigencia.getCadastroExigencia().getExigencia());
        setSubCategoriaEvento(SubCategoriaEventoCalendario.ACADEMICO);
        //setDefinicoesEstilo("eventos " + new ExigenciaDecorator(cumprimentoExigencia).getClasseEstilo());
    }

    public PrazoExigencia(MatriculaEmDisciplina matriculaEmDisciplina){
        setDescricao("");
        setDiaInicio(matriculaEmDisciplina.getInicio());
        setDiaTermino(matriculaEmDisciplina.getTermino());
        setCiclo(CICLO.UNICO);
        setODiaTodo(true);
        setTitulo(matriculaEmDisciplina.getDisciplina().getSigla());
        setSubCategoriaEvento(SubCategoriaEventoCalendario.ACADEMICO);
        //setDefinicoesEstilo("eventos " + ExigenciaDecorator.getClasseEstilo(disciplinaCursada));
    }

    @Override
    public boolean isCadastravelPeloUsuario() {
        return false;
    }

    @Override
    public PrazoExigencia getGeradorEventos() {
        return this;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public int compareTo(EventoBasico e) {
        PrazoExigencia o = (PrazoExigencia) e;
        return getDiaInicio().compareTo(e.getDiaInicio());
    }
}
