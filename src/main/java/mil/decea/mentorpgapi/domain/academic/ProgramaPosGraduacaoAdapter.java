package mil.decea.mentorpgapi.domain.academic;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import mil.decea.mentorpgapi.domain.user.UserAdapter;

@NoArgsConstructor
@Service
public class ProgramaPosGraduacaoAdapter extends AbstractEntityDTOAdapter<ProgramaPosGraduacao, ProgramaPosGraduacaoRecord> {

@Autowired
UserAdapter coordenador;


	@Override
	public ProgramaPosGraduacaoRecord generateRecord() { return new ProgramaPosGraduacaoRecord(getEntity());}

	public ProgramaPosGraduacao updateEntity() {
		getEntity().setSigla(getIdentifiedRecord().sigla());
		getEntity().setDefinicao(getIdentifiedRecord().definicao());
		getEntity().setNome(getIdentifiedRecord().nome());
		getEntity().setFatoresCondicionantes(getIdentifiedRecord().fatoresCondicionantes());
		coordenador.with(getEntity().getCoordenador(), getIdentifiedRecord().coordenador()).updateEntity();
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}