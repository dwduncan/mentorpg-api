package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
	DocumentTypeRecord tipoDocumentacao,
String previousFileName,
String motivoRecusa,
Long idExigencia,
boolean obrigatorio,
StatusDoc statusDocumento,
Long id,
boolean ativo,
String nomeArquivo,
String formato,
long tamanho,
String dataHoraUpload,
String base64Data,
String arquivoUrl) {
	public UserDocumentRecord(UserDocument obj) {
		this(new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getPreviousFileName(),
			obj.getMotivoRecusa(),
			obj.getIdExigencia(),
			obj.isObrigatorio(),
			obj.getStatusDocumento(),
			obj.getId(),
			obj.isAtivo(),
			obj.getNomeArquivo(),
			obj.getFormato(),
			obj.getTamanho(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getBase64Data(),
			obj.getArquivoUrl());
	}
}