package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import java.lang.Long;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
boolean obrigatorio,
StatusDoc statusDocumento,
String motivoRecusa,
Long idExigencia,
	DocumentTypeRecord tipoDocumentacao,
Long id,
boolean ativo,
String base64Data,
String arquivoUrl,
String formato,
String nomeArquivo,
String dataHoraUpload,
long tamanho) {
	public UserDocumentRecord(UserDocument obj) {
		this(obj.isObrigatorio(),
			obj.getStatusDocumento(),
			obj.getMotivoRecusa(),
			obj.getIdExigencia(),
			new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getId(),
			obj.isAtivo(),
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getTamanho());
	}
}