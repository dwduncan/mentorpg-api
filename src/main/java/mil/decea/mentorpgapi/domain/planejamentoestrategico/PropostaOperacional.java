package mil.decea.mentorpgapi.domain.planejamentoestrategico;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.academic.Aluno;
import mil.decea.mentorpgapi.domain.academic.Candidato;
import mil.decea.mentorpgapi.domain.academic.Pesquisador;
import mil.decea.mentorpgapi.domain.administrativo.OrgaoInstitucional;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;

import java.util.*;
import java.util.stream.Collectors;


@Table(name = "propostaoperacional", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PropostaOperacional extends SequenceIdEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Candidato candidato;
    @OneToOne(fetch = FetchType.LAZY)
    private Aluno aluno;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "propostaOperacional", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<EnquadramentoSelecionado> enquadramentosSelecionados = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "propostaOperacional", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<OrgaoDemandadoPesquisa> orgaosDemandados = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "propostaOperacional", cascade =  CascadeType.ALL, orphanRemoval = true)
    private List<TesesRelacionadas> tesesRelacionadas = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    private String outroOrgaoInstitucional;
    private boolean orgaoNaoIdentificado;

    @Transient
    private Map<TipoEnquadramento,List<EnquadramentoSelecionadoProxy>> mapaListaOpcoes;

    private boolean continuacaoOutraPesquisa;

    public PropostaOperacional(Candidato candidato){
        this.candidato = candidato;
        preencherMapa();
    }

    public PropostaOperacional(Aluno aluno){
        this.aluno = aluno;
        preencherMapa();
    }

    public Map<ItemEnquadramento,EnquadramentoSelecionadoProxy> getMapaItensEnquadrados(){
        return enquadramentosSelecionados.stream().collect(Collectors.toMap(EnquadramentoSelecionado::getItemEnquadramento, EnquadramentoSelecionadoProxy::new));
    }

    private void removeOpcoesEnquadramento(TipoEnquadramento tipo){
        List<EnquadramentoSelecionado> itensAremover = getEnquadramentosSelecionados().stream().filter(i->i.getEnquadramento() == tipo).collect(Collectors.toList());
        for(EnquadramentoSelecionado e : itensAremover) enquadramentosSelecionados.remove(e);
        mapaListaOpcoes.put(tipo,new ArrayList<>());
    }


    /**
     * Substitui todos os itens selecionados de acordo com a ordem e os itens do mapa
     * @param tipo o TipoEnquadramento que será completamente substituído
     * @param mapaIndiceItem o mapa no qual a chave é a ordem/prioridade do item selecionado que é o valor do mapa
     */
    public void setItensEnquadramento(TipoEnquadramento tipo, Map<Integer,ItemEnquadramento> mapaIndiceItem){
        List<EnquadramentoSelecionado> _upt_lista = new ArrayList<>(enquadramentosSelecionados.stream().filter(e->e.getEnquadramento() != tipo).collect(Collectors.toList()));
        List<EnquadramentoSelecionado> lista = new ArrayList<>(mapaIndiceItem.size());
        for(Integer i : mapaIndiceItem.keySet()) lista.add(new EnquadramentoSelecionado(this, mapaIndiceItem.get(i),i));
        Collections.sort(lista);
        _upt_lista.addAll(lista);
        enquadramentosSelecionados = _upt_lista;
        preencherMapa();
    }

    public void setOrgaosDemandados(Map<Integer, OrgaoInstitucional> mapaIndiceOrgao){

        orgaosDemandados = new ArrayList<>(mapaIndiceOrgao.size());
        if (!mapaIndiceOrgao.isEmpty()){
            for(Integer k : mapaIndiceOrgao.keySet()){
                orgaosDemandados.add(new OrgaoDemandadoPesquisa(mapaIndiceOrgao.get(k),this,k));
            }
        }
    }

    public void setTesesRelacionadas(Map<Integer, TesesRelacionadas> mapaIndiceTeses){

        tesesRelacionadas = new ArrayList<>(mapaIndiceTeses.size());
        if (!mapaIndiceTeses.isEmpty()){
            for(Integer k : mapaIndiceTeses.keySet()){
                TesesRelacionadas t = mapaIndiceTeses.get(k);
                t.setPropostaOperacional(this);
                tesesRelacionadas.add(t);
            }
        }
    }

    public void addOrUpdateTeseRelacionada(TesesRelacionadas tese){
        TesesRelacionadas t = getPorIdTeseDissertacao(tese.getIdTeseDissertacao());
        if (t != null){
            tesesRelacionadas.remove(t);
        }
        tesesRelacionadas.add(tese);
    }

    public void removerTeseRelacionada(TesesRelacionadas tese){
        tesesRelacionadas.remove(tese);
    }

    public void removerTeseRelacionada(Long idTeseDissertacao){
        TesesRelacionadas t = getPorIdTeseDissertacao(idTeseDissertacao);
        if (t != null){
            tesesRelacionadas.remove(t);
        }
    }


    /**
     *
     * @param idTeseDissertacao
     * @return a instância da TesesRelacionadas referente a TeseDissertacao com o idTeseDissertacao ou null se a referida
     * TeseDissertacao não estiver relacionada na lista.
     */
    public TesesRelacionadas getPorIdTeseDissertacao(Long idTeseDissertacao){
        return getTesesRelacionadas().stream().filter(t->t.getIdTeseDissertacao().equals(idTeseDissertacao)).findAny().orElse(null);
    }

    public void setTesesRelacionadas(List<TesesRelacionadas> tesesRelacionadas){
        this.tesesRelacionadas = tesesRelacionadas;
    }

    public void preencherMapa(){

        Collections.sort(enquadramentosSelecionados);
        mapaListaOpcoes = new HashMap<>(TipoEnquadramento.values().length);
        for (TipoEnquadramento t : TipoEnquadramento.values()) mapaListaOpcoes.put(t,new ArrayList<>());

        Map<TipoEnquadramento,Integer> indexador = new HashMap<>();
        for(EnquadramentoSelecionado opcao : getEnquadramentosSelecionados()){
            Integer ord = indexador.compute(opcao.getEnquadramento(), (k, v) -> v == null ? 0 : v + 1);
            opcao.setOrdem(ord);
            List<EnquadramentoSelecionadoProxy> opcoes = mapaListaOpcoes.get(opcao.getEnquadramento());
            opcoes.add(new EnquadramentoSelecionadoProxy(opcao));
        }
    }

    public List<? extends AbstractEnquadramentoSelecionado> getOpcoesEnquadramento(TipoEnquadramento tipoEnquadramento){
        if (mapaListaOpcoes == null) preencherMapa();
        return mapaListaOpcoes.get(tipoEnquadramento);
    }

    public Candidato getCandidato() {
        return candidato;
    }

    public void setCandidato(Candidato candidato) {
        this.candidato = candidato;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public List<EnquadramentoSelecionado> getEnquadramentosSelecionados() {
        return enquadramentosSelecionados;
    }

    @PrePersist
    @PreUpdate
    public void preSalvar(){
        if (getCandidato() == null && getAluno() == null){
            throw new MentorValidationException("É obrigatório definir um candidato ou um aluno");
        }
        if (getCandidato() != null && getAluno() != null){
            throw new MentorValidationException("É obrigatório definir ou um candidato ou um aluno. " +
                    "Não é permitido utilizar a mesma proposta para um aluno e um candidato simultaneamente");
        }
    }

    public List<OrgaoDemandadoPesquisa> getOrgaosDemandados() {
        return orgaosDemandados;
    }

    public boolean isOrgaoNaoIdentificado() {
        return orgaoNaoIdentificado;
    }

    public void setOrgaoNaoIdentificado(boolean orgaoNaoIdentificado) {
        this.orgaoNaoIdentificado = orgaoNaoIdentificado;
    }

    public String getOutroOrgaoInstitucional() {
        return outroOrgaoInstitucional;
    }

    public void setOutroOrgaoInstitucional(String outroOrgaoInstitucional) {
        this.outroOrgaoInstitucional = outroOrgaoInstitucional;
    }

    public List<TipoEnquadramento> getTiposEnquadramento(){
        return Arrays.asList(TipoEnquadramento.values());
    }

    public List<TesesRelacionadas> getTesesRelacionadas() {
        return tesesRelacionadas;
    }

    @Override
    public PropostaOperacional clone() {
        PropostaOperacional clone = (PropostaOperacional) super.clone();
        clone.setCandidato(null);
        clone.setAluno(null);
        clone.orgaosDemandados = new ArrayList<>();
        clone.enquadramentosSelecionados = new ArrayList<>();
        clone.tesesRelacionadas = new ArrayList<>();

        for(TesesRelacionadas t : getTesesRelacionadas()){
            TesesRelacionadas tc = t.clone();
            tc.setPropostaOperacional(clone);
            clone.tesesRelacionadas.add(tc);
        }

        for(OrgaoDemandadoPesquisa t : getOrgaosDemandados()){
            OrgaoDemandadoPesquisa tc = t.clone();
            tc.setPropostaOperacional(clone);
            clone.orgaosDemandados.add(tc);
        }

        for(EnquadramentoSelecionado t : getEnquadramentosSelecionados()){
            EnquadramentoSelecionado tc = t.clone();
            tc.setPropostaOperacional(clone);
            clone.enquadramentosSelecionados.add(tc);
        }

        return clone;
    }

    @Override
    public String getEntityDescriptor() {
        return "Proposta operacional " + ((Pesquisador)getParentObject()).getUser().getNomeQualificado();
    }

    @Override
    public TrackedEntity getParentObject() {
        return aluno != null ? aluno : candidato;
    }
}
