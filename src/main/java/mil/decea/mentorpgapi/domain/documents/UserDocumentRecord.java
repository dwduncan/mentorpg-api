package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
	DocumentTypeRecord tipoDocumentacao,
boolean obrigatorio,
@NotNull(message="{NotNull.message}", payload={}, groups={})
User user,
String motivoRecusa,
StatusDoc statusDocumento,
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
			obj.isObrigatorio(),
			obj.getUser(),
			obj.getMotivoRecusa(),
			obj.getStatusDocumento(),
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