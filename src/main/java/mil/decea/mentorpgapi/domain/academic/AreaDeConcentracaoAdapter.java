package mil.decea.mentorpgapi.domain.academic;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;import mil.decea.mentorpgapi.domain.user.User;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;


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
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}