package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
	DocumentTypeRecord tipoDocumentacao,
String storageDestinationPath,
String motivoRecusa,
String bucket,
Long userId,
StatusDoc statusDocumento,
Long idExigencia,
boolean obrigatorio,
Long id,
boolean ativo,
String previousFileName,
String nomeArquivo,
String dataHoraUpload,
String formato,
String arquivoUrl,
String base64Data,
long tamanho) {
	public UserDocumentRecord(UserDocument obj) {
		this(new DocumentTypeRecord(obj.getTipoDocumentacao()),
			"usr_" + obj.getUser().getId() +"/type_"+obj.getTipoDocumentacao().getId()+"/"+obj.getNomeArquivo(),
			obj.getMotivoRecusa(),
			"userdocuments",
			obj.getUser().getId(),
			obj.getStatusDocumento(),
			obj.getIdExigencia(),
			obj.isObrigatorio(),
			obj.getId(),
			obj.isAtivo(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getFormato(),
			obj.getArquivoUrl(),
			obj.getBase64Data(),
			obj.getTamanho());
	}
}