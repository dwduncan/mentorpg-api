package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserImageRecord(
Long id,
String previousFileName,
String nomeArquivo,
String dataHoraUpload,
String base64Data,
String arquivoUrl,
String formato,
long tamanho) implements IdentifiedRecord {
	public UserImageRecord(UserImage obj) {
		this(obj.getId(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			obj.getTamanho());
	}
}