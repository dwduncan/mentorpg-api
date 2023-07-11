package mil.decea.mentorpgapi.domain.academic.adapters;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.academic.AreaDeConcentracao;
import mil.decea.mentorpgapi.domain.academic.records.AreaDeConcentracaoRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import mil.decea.mentorpgapi.domain.user.UserAdapter;

@NoArgsConstructor
@Service
public class AreaDeConcentracaoAdapter extends AbstractEntityDTOAdapter<AreaDeConcentracao, AreaDeConcentracaoRecord> {

@Autowired
ProgramaPosGraduacaoAdapter programa;

@Autowired
UserAdapter representanteDaArea;


	@Override
	public AreaDeConcentracaoRecord generateRecord() { return new AreaDeConcentracaoRecord(getEntity());}

	public AreaDeConcentracao updateEntity() {
		getEntity().setSigla(getIdentifiedRecord().sigla());
		programa.with(getEntity().getPrograma(), getIdentifiedRecord().programa()).updateEntity();
		getEntity().setDefinicao(getIdentifiedRecord().definicao());
		getEntity().setNome(getIdentifiedRecord().nome());
		representanteDaArea.with(getEntity().getRepresentanteDaArea(), getIdentifiedRecord().representanteDaArea()).updateEntity();
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}