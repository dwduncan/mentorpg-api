package mil.decea.mentorpgapi.domain.academic;


/**
 * Força Aérea Brasileira - (FAB)
 * Instituto Tecnológico de Aeronáutica - (ITA)
 * Programa de Pós-Graduação em Aplicações Operacionais - (PPGAO)
 * PPGAO-MENTOR
 * Author: Dennys Wallace Duncan Imbassahy
 * (Ten Cel Av Duncan)
 * Email: dennysduncan@gmail.com  /  dwduncan@ita.br
 * Date: 10/04/2021
 * Time: 14:02
 */
public enum StatusAluno  {

    VERIFICAR("Verificar",true),
    EM_CURSO("Em Curso",true),
    APROVADO("Aprovado",false),
    APROVADO_FORAPRAZO("Aprovado(*)",false),
    EXCLUIDO("Excluído",false),
    DESISTENCIA("Desistência",false),
    PENDENTE("Pendente",true),
    LICENCA("Sob licença",false),
    JUBILADO("Jubilado",false),
    REPROVADO("Reprovado",false);

    private String nome;
    private boolean exclusivo;

    StatusAluno(String n,boolean exclusivo){
        setNome(n);
        this.exclusivo = exclusivo;
    }

    public String getRotulo() {
        return nome;
    }

    public Long getId() {
        return (long) ordinal();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isExclusivo() {
        return exclusivo;
    }
}
