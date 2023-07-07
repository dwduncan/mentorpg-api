package mil.decea.mentorpgapi.domain.documents;


import lombok.NoArgsConstructor;
import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Service
public class UserDocumentAdapter extends AbstractEntityDTOAdapter<UserDocument, UserDocumentRecord> {

@Autowired
DocumentTypeAdapter tipoDocumentacao;


	@Override
	public UserDocumentRecord generateRecord() { return new UserDocumentRecord(getEntity());}

	public UserDocument updateEntity() {
		tipoDocumentacao.with(getEntity().getTipoDocumentacao(), getIdentifiedRecord().tipoDocumentacao()).updateEntity();
		getEntity().setIdExigencia(getIdentifiedRecord().idExigencia());
		getEntity().setObrigatorio(getIdentifiedRecord().obrigatorio());
		getEntity().setMotivoRecusa(getIdentifiedRecord().motivoRecusa());
		getEntity().setStatusDocumento(getIdentifiedRecord().statusDocumento());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setPreviousFileName(getIdentifiedRecord().previousFileName());
		getEntity().setNomeArquivo(getIdentifiedRecord().nomeArquivo());
		getEntity().setFormato(getIdentifiedRecord().formato());
		getEntity().setTamanho(getIdentifiedRecord().tamanho());
		getEntity().setBase64Data(getIdentifiedRecord().base64Data());
		getEntity().setArquivoUrl(getIdentifiedRecord().arquivoUrl());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}