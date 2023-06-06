package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record UserImageRecord(
	String base64Data,
	String arquivoUrl,
	String formato,
	String dataHoraUpload,
	String nomeArquivo) {
	public UserImageRecord(UserImage obj) {
		this(obj.getBase64Data(),
			obj.getArquivoUrl(),
			obj.getFormato(),
			DateTimeAPIHandler.converter(obj.getDataHoraUpload())+"",
			obj.getNomeArquivo());
	}
}