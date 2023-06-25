package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserImageRecord(
Long id,
String base64Data,
String arquivoUrl,
String formato,
String nomeArquivo,
String dataHoraUpload,
long tamanho) implements IdentifiedRecord {
	public UserImageRecord(UserImage obj) {
		this(obj.getId(),
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getTamanho());
	}
}