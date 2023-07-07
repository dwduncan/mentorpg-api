package mil.decea.mentorpgapi.domain.academic;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataAdapter;
import mil.decea.mentorpgapi.domain.user.UserAdapter;

@NoArgsConstructor
@Service
public class AreaDePesquisaAdapter extends AbstractEntityDTOAdapter<AreaDePesquisa, AreaDePesquisaRecord> {

@Autowired
EmbeddedExternalDataAdapter imagemAreaDePesquisa;

@Autowired
UserAdapter coordenadorDaArea;


	@Override
	public AreaDePesquisaRecord generateRecord() { return new AreaDePesquisaRecord(getEntity());}

	public AreaDePesquisa updateEntity() {
		imagemAreaDePesquisa.with(getEntity().getImagemAreaDePesquisa(), getIdentifiedRecord().imagemAreaDePesquisaRecord()).updateEntity();
		coordenadorDaArea.with(getEntity().getCoordenadorDaArea(), getIdentifiedRecord().coordenadorDaArea()).updateEntity();
		getEntity().setConceito(getIdentifiedRecord().conceito());
		getEntity().setNomeExtenso(getIdentifiedRecord().nomeExtenso());
		getEntity().setEncerrado(getIdentifiedRecord().encerrado());
		getEntity().setSigla(getIdentifiedRecord().sigla());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}