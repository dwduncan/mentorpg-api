package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
	DocumentTypeRecord tipoDocumentacao,
String storageDestinationPath,
StatusDoc statusDocumento,
boolean obrigatorio,
Long idExigencia,
String motivoRecusa,
Long userId,
String bucket,
Long id,
boolean ativo,
String previousFileName,
String nomeArquivo,
String lastUpdate,
String formato,
String arquivoUrl,
long tamanho,
String base64Data) implements IdentifiedRecord {
	public UserDocumentRecord(UserDocument obj) {
		this(new DocumentTypeRecord(obj.getTipoDocumentacao()),
			"usr_" + obj.getUser().getId() +"/type_"+obj.getTipoDocumentacao().getId()+"/"+obj.getNomeArquivo(),
			obj.getStatusDocumento(),
			obj.isObrigatorio(),
			obj.getIdExigencia(),
			obj.getMotivoRecusa(),
			obj.getUser().getId(),
			"userdocuments",
			obj.getId(),
			obj.isAtivo(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"",
			obj.getFormato(),
			obj.getArquivoUrl(),
			obj.getTamanho(),
			obj.getBase64Data());
	}
}