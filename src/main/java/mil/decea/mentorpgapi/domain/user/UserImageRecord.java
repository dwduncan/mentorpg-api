package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.EmbeddedImage;
import mil.decea.mentorpgapi.domain.ExternalDataRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserImageRecord(
Long id,
String previousFileName,
String nomeArquivo,
String lastUpdate,
String base64Data,
String arquivoUrl,
String formato,
long tamanho) implements ExternalDataRecord {
	public UserImageRecord(EmbeddedImage obj) {
		this(obj.getId(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"",
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			obj.getTamanho());
	}
}