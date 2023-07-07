package mil.decea.mentorpgapi.domain.documents;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
DocumentTypeRecord tipoDocumentacao,
String motivoRecusa,
boolean obrigatorio,
Long idExigencia,
StatusDoc statusDocumento,
String storageDestinationPath,
Long userId,
String bucket,
Long id,
boolean ativo,
String nomeArquivo,
String previousFileName,
String lastUpdate,
long tamanho,
String arquivoUrl,
String formato,
String base64Data) implements IdentifiedRecord {
	public UserDocumentRecord(UserDocument obj) {
		this(new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getMotivoRecusa(),
			obj.isObrigatorio(),
			obj.getIdExigencia(),
			obj.getStatusDocumento(),
			"usr_" + obj.getUser().getId() +"/type_"+obj.getTipoDocumentacao().getId()+"/"+obj.getNomeArquivo(),
			obj.getUser().getId(),
			"userdocuments",
			obj.getId(),
			obj.isAtivo(),
			obj.getNomeArquivo(),
			obj.getPreviousFileName(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"",
			obj.getTamanho(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			obj.getBase64Data());
	}
}