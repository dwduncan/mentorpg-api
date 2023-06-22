package mil.decea.mentorpgapi.domain.documents;
import java.lang.Long;

import mil.decea.mentorpgapi.domain.daoservices.datageneration.OptionalsRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.etc.security.TokenService;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;


@OptionalsRecordField
public record ExternalDocumentRecord(
Long id,
boolean ativo,
String previousFileName,
String nomeArquivo,
String arquivoUrl,
String base64Data,
String formato,
String dataHoraUpload,
long tamanho,
String bucket,
int duracaoEmSegundos,
String storageDestinationPath,
StatusDoc statusDocumento,
String motivoRecusa,
DocumentTypeRecord documentTypeRecord,
Long userId) {
	public ExternalDocumentRecord(ExternalDataEntity obj) {
		this(obj.getId(),
			obj.isAtivo(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			obj.getArquivoUrl(),
			obj.getBase64Data(),
			obj.getFormato(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getTamanho(),
			obj.getBucket(),
			TokenService.EXPIRATION_TIME_IN_MINUTES,
			obj.getStorageDestinationPath(),
			null,
			"",
				null,
	  null);
	}

	public ExternalDocumentRecord(Long id, boolean ativo) {
		this(id,
			ativo,
			null,
			null,
			null,
			null,
			null,
			null,
			0,
			null,
			TokenService.EXPIRATION_TIME_IN_MINUTES,
			null,
			null,
			"",
			null,
			null);
	}

	public ExternalDocumentRecord(UserDocument obj) {
		this(obj.getId(),
				obj.isAtivo(),
				obj.getPreviousFileName(),
				obj.getNomeArquivo(),
				obj.getArquivoUrl(),
				obj.getBase64Data(),
				obj.getFormato(),
				DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
				obj.getTamanho(),
				obj.getBucket(),
				TokenService.EXPIRATION_TIME_IN_MINUTES,
				obj.getStorageDestinationPath(),
				obj.getStatusDocumento(),
				obj.getMotivoRecusa(),
				new DocumentTypeRecord(obj.getTipoDocumentacao()),
				obj.getUser().getId());
	}

	public ExternalDocumentRecord(Long id,String url, int duracaoEmSegundos, String storageDestinationPath,
								  String bucket, String nomeArquivo){
		this(id,
				true,
				null,
				nomeArquivo,
				url,
				null,
				null,
				null,
				0,
				bucket,
				duracaoEmSegundos,
				storageDestinationPath,
				null,
				"",
				null,
				null);
	}
}