package mil.decea.mentorpgapi.domain.academic;

import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Service
public class AreaDeConcentracaoAdapter extends AbstractEntityDTOAdapter<AreaDeConcentracao, AreaDeConcentracaoRecord> {


	@Override
	public AreaDeConcentracaoRecord generateRecord() { return new AreaDeConcentracaoRecord(getEntity());}

	public AreaDeConcentracao updateEntity() {
		getEntity().setRepresentanteDaArea(getIdentifiedRecord().representanteDaArea());
		getEntity().setSigla(getIdentifiedRecord().sigla());
		getEntity().setNome(getIdentifiedRecord().nome());
		getEntity().setPrograma(getIdentifiedRecord().programa());
		getEntity().setDefinicao(getIdentifiedRecord().definicao());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		return getEntity();
	}
}