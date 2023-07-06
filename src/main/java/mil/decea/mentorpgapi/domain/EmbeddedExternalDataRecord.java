package mil.decea.mentorpgapi.domain;

import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record EmbeddedExternalDataRecord(
Long id,
String previousFileName,
String nomeArquivo,
String dataHoraUpload,
String base64Data,
String arquivoUrl,
String formato,
long tamanho) implements IdentifiedRecord {
	public EmbeddedExternalDataRecord(EmbeddedExternalData obj) {
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