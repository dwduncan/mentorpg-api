package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import java.lang.Long;

import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
String storageDestinationPath,
String bucket,
StatusDoc statusDocumento,
Long userId,
boolean obrigatorio,
String motivoRecusa,
Long idExigencia,
	DocumentTypeRecord tipoDocumentacao,
Long id,
boolean ativo,
String previousFileName,
String nomeArquivo,
String dataHoraUpload,
String base64Data,
String arquivoUrl,
String formato,
long tamanho) implements IdentifiedRecord {
	public UserDocumentRecord(UserDocument obj) {
		this("usr_" + obj.getUser().getId() +"/type_"+obj.getTipoDocumentacao().getId()+"/"+obj.getNomeArquivo(),
			"userdocuments",
			obj.getStatusDocumento(),
			obj.getUser().getId(),
			obj.isObrigatorio(),
			obj.getMotivoRecusa(),
			obj.getIdExigencia(),
			new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getId(),
			obj.isAtivo(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"",
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			obj.getTamanho());
	}
}