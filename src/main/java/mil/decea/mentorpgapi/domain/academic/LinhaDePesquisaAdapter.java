package mil.decea.mentorpgapi.domain.academic;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@NoArgsConstructor
@Service
public class LinhaDePesquisaAdapter extends AbstractEntityDTOAdapter<LinhaDePesquisa, LinhaDePesquisaRecord> {

@Autowired
AreaDePesquisaAdapter areaDePesquisa;


	@Override
	public LinhaDePesquisaRecord generateRecord() { return new LinhaDePesquisaRecord(getEntity());}

	public LinhaDePesquisa updateEntity() {
		getEntity().setNome(getIdentifiedRecord().nome());
		getEntity().setConceito(getIdentifiedRecord().conceito());
		areaDePesquisa.with(getEntity().getAreaDePesquisa(), getIdentifiedRecord().areaDePesquisa()).updateEntity();
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}