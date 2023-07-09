package mil.decea.mentorpgapi.domain.user;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataAdapter;

@NoArgsConstructor
@Service
public class UserAdapter extends AbstractEntityDTOAdapter<User, UserRecord_old> {

@Autowired
EmbeddedExternalDataAdapter userImage;

	@Override
	public UserRecord_old generateRecord() { return new UserRecord_old(getEntity());}

	public User updateEntity() {
		getEntity().setAntiguidadeRelativa(getIdentifiedRecord().antiguidadeRelativa());
		getEntity().setCpf(getIdentifiedRecord().cpf());
		userImage.with(getEntity().getUserImage(), getIdentifiedRecord().userImageRecord()).updateEntity();
		getEntity().setQuadro(getIdentifiedRecord().quadro());
		getEntity().setPttc(getIdentifiedRecord().pttc());
		getEntity().setNomeCompleto(getIdentifiedRecord().nomeCompleto());
		getEntity().setSexo(getIdentifiedRecord().sexo());
		getEntity().setNomeGuerra(getIdentifiedRecord().nomeGuerra());
		getEntity().setEspecialidade(getIdentifiedRecord().especialidade());
		getEntity().setPosto(getIdentifiedRecord().posto());
		getEntity().setNomeQualificado(getIdentifiedRecord().nomeQualificado());
		getEntity().setCelular(getIdentifiedRecord().celular());
		getEntity().setIdentidade(getIdentifiedRecord().identidade());
		getEntity().setTitulacao(getIdentifiedRecord().titulacao());
		getEntity().setUltimaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().ultimaPromocao()));
		getEntity().setForcaSingular(getIdentifiedRecord().forcaSingular());
		getEntity().setRole(getIdentifiedRecord().role());
		getEntity().setEmail(getIdentifiedRecord().email());
		getEntity().setSaram(getIdentifiedRecord().saram());
		getEntity().setDataPraca(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataPraca()));
		getEntity().setProximaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().proximaPromocao()));
		getEntity().setObservacoes(getIdentifiedRecord().observacoes());
		getEntity().setDataNascimento(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataNascimento()));
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}