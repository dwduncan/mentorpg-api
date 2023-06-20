package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.documents.DocumentTypeRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserDocumentRecord(
String storageDestinationPath,
	DocumentTypeRecord tipoDocumentacao,
Long idExigencia,
StatusDoc statusDocumento,
String motivoRecusa,
boolean obrigatorio,
String bucket,
Long id,
boolean ativo,
String previousFileName,
long tamanho,
String formato,
String dataHoraUpload,
String base64Data,
String nomeArquivo,
String arquivoUrl) {
	public UserDocumentRecord(UserDocument obj) {
		this("usr_" + obj.getUser().getId() +"/type_"+obj.getTipoDocumentacao().getId()+"/"+obj.getNomeArquivo(),
			new DocumentTypeRecord(obj.getTipoDocumentacao()),
			obj.getIdExigencia(),
			obj.getStatusDocumento(),
			obj.getMotivoRecusa(),
			obj.isObrigatorio(),
			"userdocuments",
			obj.getId(),
			obj.isAtivo(),
			obj.getPreviousFileName(),
			obj.getTamanho(),
			obj.getFormato(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getBase64Data(),
			obj.getNomeArquivo(),
			obj.getArquivoUrl());
	}
}