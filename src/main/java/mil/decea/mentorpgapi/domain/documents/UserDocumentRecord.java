package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
	DocumentTypeRecord tipoDocumentacao,
StatusDoc statusDocumento,
String motivoRecusa,
boolean obrigatorio,
Long idExigencia,
Long id,
boolean ativo,
String formato,
String nomeArquivo,
String dataHoraUpload,
String arquivoUrl,
String base64Data) {
	public UserDocumentRecord(UserDocument obj) {
		this(new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getStatusDocumento(),
			obj.getMotivoRecusa(),
			obj.isObrigatorio(),
			obj.getIdExigencia(),
			obj.getId(),
			obj.isAtivo(),
			obj.getFormato(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getArquivoUrl(),
			obj.getBase64Data());
	}
}