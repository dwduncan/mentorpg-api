package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
String storageDestinationPath,
String bucket,
	DocumentTypeRecord tipoDocumentacao,
Long userId,
String motivoRecusa,
Long idExigencia,
StatusDoc statusDocumento,
boolean obrigatorio,
Long id,
boolean ativo,
String previousFileName,
String nomeArquivo,
String formato,
String dataHoraUpload,
String base64Data,
String arquivoUrl,
long tamanho) implements IdentifiedRecord {
	public UserDocumentRecord(UserDocument obj) {
		this("usr_" + obj.getUser().getId() +"/type_"+obj.getTipoDocumentacao().getId()+"/"+obj.getNomeArquivo(),
			"userdocuments",
			new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getUser().getId(),
			obj.getMotivoRecusa(),
			obj.getIdExigencia(),
			obj.getStatusDocumento(),
			obj.isObrigatorio(),
			obj.getId(),
			obj.isAtivo(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			obj.getFormato(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getTamanho());
	}
}