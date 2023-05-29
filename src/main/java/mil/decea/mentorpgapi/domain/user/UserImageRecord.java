package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.util.ConvertDateToMillis;

public record UserImageRecord(
	String formato,
	String nomeArquivo,
	String dataHoraUpload,
	String arquivoUrl,
	String base64Data) {
	public UserImageRecord(UserImage obj) {
		this(obj.getFormato(),
			obj.getNomeArquivo(),
			ConvertDateToMillis.converter(obj.getDataHoraUpload())+"",
			obj.getArquivoUrl(),
			obj.getBase64Data());
	}
}