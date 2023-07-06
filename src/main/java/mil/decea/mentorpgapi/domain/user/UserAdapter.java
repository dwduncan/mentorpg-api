package mil.decea.mentorpgapi.domain.user;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.EmbeddedImageAdapter;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import java.lang.Long;


@NoArgsConstructor
@Service
public class UserAdapter extends AbstractEntityDTOAdapter<User, UserRecord> {

@Autowired
EmbeddedImageAdapter embeddedImageAdapter;


	@Override
	public UserRecord generateRecord() { return new UserRecord(getEntity());}

	public User updateEntity() {
		getEntity().setAntiguidadeRelativa(getIdentifiedRecord().antiguidadeRelativa());
		getEntity().setNomeCompleto(getIdentifiedRecord().nomeCompleto());
		getEntity().setNomeQualificado(getIdentifiedRecord().nomeQualificado());,
		
		getEntity().setDocuments(getIdentifiedRecord().documents().stream().map(r-> new UserDocument(r)).toList());
		getEntity().setDataNascimento(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataNascimento()));
		getEntity().setCelular(getIdentifiedRecord().celular());
		getEntity().setPosto(getIdentifiedRecord().posto());
		getEntity().setForcaSingular(getIdentifiedRecord().forcaSingular());
		getEntity().setEmail(getIdentifiedRecord().email());
		getEntity().setObservacoes(getIdentifiedRecord().observacoes());
		getEntity().setRole(getIdentifiedRecord().role());
		getEntity().setEspecialidade(getIdentifiedRecord().especialidade());
		getEntity().setNomeGuerra(getIdentifiedRecord().nomeGuerra());
		getEntity().setIdentidade(getIdentifiedRecord().identidade());
		getEntity().setTitulacao(getIdentifiedRecord().titulacao());
		getEntity().setProximaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().proximaPromocao()));
		getEntity().setUltimaPromocao(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().ultimaPromocao()));
		getEntity().setDataPraca(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().dataPraca()));
		getEntity().setSexo(getIdentifiedRecord().sexo());
		getEntity().setSaram(getIdentifiedRecord().saram());
		embeddedImageAdapter.with(getEntity().getUserImage(), getIdentifiedRecord().userImageRecord()).updateEntity();
		getEntity().setPttc(getIdentifiedRecord().pttc());
		getEntity().setQuadro(getIdentifiedRecord().quadro());
		getEntity().setCpf(getIdentifiedRecord().cpf());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		return getEntity();
	}
}