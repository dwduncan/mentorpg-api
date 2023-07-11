package mil.decea.mentorpgapi.domain.academic.adapters;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.academic.AreaDePesquisa;
import mil.decea.mentorpgapi.domain.academic.records.AreaDePesquisaRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import mil.decea.mentorpgapi.domain.user.UserAdapter;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataAdapter;

@NoArgsConstructor
@Service
public class AreaDePesquisaAdapter extends AbstractEntityDTOAdapter<AreaDePesquisa, AreaDePesquisaRecord> {

@Autowired
UserAdapter coordenadorDaArea;

@Autowired
EmbeddedExternalDataAdapter imagemAreaDePesquisa;


	@Override
	public AreaDePesquisaRecord generateRecord() { return new AreaDePesquisaRecord(getEntity());}

	public AreaDePesquisa updateEntity() {
		getEntity().setSigla(getIdentifiedRecord().sigla());
		coordenadorDaArea.with(getEntity().getCoordenadorDaArea(), getIdentifiedRecord().coordenadorDaArea()).updateEntity();
		imagemAreaDePesquisa.with(getEntity().getImagemAreaDePesquisa(), getIdentifiedRecord().imagemAreaDePesquisaRecord()).updateEntity();
		getEntity().setConceito(getIdentifiedRecord().conceito());
		getEntity().setNomeExtenso(getIdentifiedRecord().nomeExtenso());
		getEntity().setEncerrado(getIdentifiedRecord().encerrado());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}