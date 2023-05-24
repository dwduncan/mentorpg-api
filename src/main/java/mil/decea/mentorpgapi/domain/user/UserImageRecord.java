package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.util.ConvertDateToMillis;

public record UserImageRecord(
	String nomeArquivo,
	String formato,
	String dataHoraUpload,
	String arquivoUrl) {
	public UserImageRecord(UserImage obj) {
		this(obj.getNomeArquivo(),
			obj.getFormato(),
			ConvertDateToMillis.converter(obj.getDataHoraUpload())+"",
			obj.getArquivoUrl());
	}
}