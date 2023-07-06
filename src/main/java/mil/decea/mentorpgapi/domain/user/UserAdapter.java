package mil.decea.mentorpgapi.domain.user;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import java.lang.Long;


@NoArgsConstructor
@Service
public class UserAdapter extends AbstractEntityDTOAdapter<User, UserRecord> {

@Autowired
EmbeddedExternalDataAdapter userImage;

	@Override
	public UserRecord generateRecord() { return new UserRecord(getEntity());}

	public User updateEntity() {
		getEntity().setCpf(getIdentifiedRecord().cpf());
		userImage.with(getEntity().getUserImage(), getIdentifiedRecord().userImageRecord()).updateEntity();
		getEntity().setPttc(getIdentifiedRecord().pttc());
		getEntity().setQuadro(getIdentifiedRecord().quadro());
		getEntity().setNomeCompleto(getIdentifiedRecord().nomeCompleto());
		getEntity().setNomeQualificado(getIdentifiedRecord().nomeQualificado());
		getEntity().setAntiguidadeRelativa(getIdentifiedRecord().antiguidadeRelativa());
		getEntity().setForcaSingular(getIdentifiedRecord().forcaSingular());
		getEntity().setTitulacao(getIdentifiedRecord().titulacao());
		getEntity().setPosto(getIdentifiedRecord().posto());
		getEntity().setSexo(getIdentifiedRecord().sexo());
		getEntity().setNomeGuerra(getIdentifiedRecord().nomeGuerra());
		getEntity().setEmail(getIdentifiedRecord().email());
		getEntity().setEspecialidade(getIdentifiedRecord().especialidade());
		getEntity().setIdentidade(getIdentifiedRecord().identidade());
		getEntity().setSaram(getIdentifiedRecord().saram());
		getEntity().setRole(getIdentifiedRecord().role());
		getEntity().setDataNascimento(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataNascimento()));
		getEntity().setCelular(getIdentifiedRecord().celular());
		getEntity().setObservacoes(getIdentifiedRecord().observacoes());
		getEntity().setUltimaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().ultimaPromocao()));
		getEntity().setProximaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().proximaPromocao()));
		getEntity().setDataPraca(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataPraca()));
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}