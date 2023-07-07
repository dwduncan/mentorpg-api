package mil.decea.mentorpgapi.domain.user;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataAdapter;

@NoArgsConstructor
@Service
public class UserAdapter extends AbstractEntityDTOAdapter<User, UserRecord> {

@Autowired
EmbeddedExternalDataAdapter userImageRecord;


	@Override
	public UserRecord generateRecord() { return new UserRecord(getEntity());}

	public User updateEntity() {
		getEntity().setAntiguidadeRelativa(getIdentifiedRecord().antiguidadeRelativa());
		getEntity().setNomeQualificado(getIdentifiedRecord().nomeQualificado());
		getEntity().setQuadro(getIdentifiedRecord().quadro());
		getEntity().setPttc(getIdentifiedRecord().pttc());
		getEntity().setCpf(getIdentifiedRecord().cpf());
		userImageRecord.with(getEntity().getUserImageRecord(), getIdentifiedRecord().userImageRecordRecord()).updateEntity();
		getEntity().setPosto(getIdentifiedRecord().posto());
		getEntity().setNomeGuerra(getIdentifiedRecord().nomeGuerra());
		getEntity().setNomeCompleto(getIdentifiedRecord().nomeCompleto());
		getEntity().setTitulacao(getIdentifiedRecord().titulacao());
		getEntity().setEspecialidade(getIdentifiedRecord().especialidade());
		getEntity().setUltimaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().ultimaPromocao()));
		getEntity().setRole(getIdentifiedRecord().role());
		getEntity().setObservacoes(getIdentifiedRecord().observacoes());
		getEntity().setEmail(getIdentifiedRecord().email());
		getEntity().setIdentidade(getIdentifiedRecord().identidade());
		getEntity().setSexo(getIdentifiedRecord().sexo());
		getEntity().setForcaSingular(getIdentifiedRecord().forcaSingular());
		getEntity().setProximaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().proximaPromocao()));
		getEntity().setDataPraca(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataPraca()));
		getEntity().setCelular(getIdentifiedRecord().celular());
		getEntity().setDataNascimento(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataNascimento()));
		getEntity().setSaram(getIdentifiedRecord().saram());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}