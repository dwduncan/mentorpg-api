package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserImageRecord(
Long id,
String previousFileName,
String nomeArquivo,
String formato,
String dataHoraUpload,
String base64Data,
String arquivoUrl,
long tamanho) implements IdentifiedRecord {
	public UserImageRecord(UserImage obj) {
		this(obj.getId(),
			obj.getPreviousFileName(),
			obj.getNomeArquivo(),
			obj.getFormato(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getTamanho());
	}
}