package mil.decea.mentorpgapi.domain.documents;


import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Service
public class UserDocumentAdapter extends AbstractEntityDTOAdapter<UserDocument, UserDocumentRecord> {


	@Override
	public UserDocumentRecord generateRecord() { return new UserDocumentRecord(getEntity());}

	public UserDocument updateEntity() {
		getEntity().setTipoDocumentacao(new DocumentType(getIdentifiedRecord().tipoDocumentacao()));
		getEntity().setStatusDocumento(getIdentifiedRecord().statusDocumento());
		getEntity().setObrigatorio(getIdentifiedRecord().obrigatorio());
		getEntity().setIdExigencia(getIdentifiedRecord().idExigencia());
		getEntity().setMotivoRecusa(getIdentifiedRecord().motivoRecusa());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setPreviousFileName(getIdentifiedRecord().previousFileName());
		getEntity().setNomeArquivo(getIdentifiedRecord().nomeArquivo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		getEntity().setFormato(getIdentifiedRecord().formato());
		getEntity().setArquivoUrl(getIdentifiedRecord().arquivoUrl());
		getEntity().setTamanho(getIdentifiedRecord().tamanho());
		getEntity().setBase64Data(getIdentifiedRecord().base64Data());
		return getEntity();
	}
}