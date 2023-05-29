package mil.decea.mentorpgapi.domain.user;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.ConvertDateToMillis;
import java.util.List;
import java.lang.Long;

public record UserRecord(
	int antiguidadeRelativa,
	Titulacao titulacao,
	@NotNull(message="Informe o nome completo")
	String nomeCompleto,
	@NotNull(message="Informe um CPF válido")
	String cpf,
	UserImageRecord userImageRecord,
	Posto posto,
	String quadro,
	String especialidade,
	@NotNull(message="Informe o nome de guerra")
	String nomeGuerra,
	Sexo sexo,
	String ultimaPromocao,
	String email,
	boolean pttc,
	String saram,
	String identidade,
	String celular,
	String dataPraca,
	String senha,
	List<?> documents,
	String dataNascimento,
	String role,
	String proximaPromocao,
	String observacoes,
	ForcaSingular forcaSingular,
	Long id,
	boolean ativo) {
	public UserRecord(User obj) {
		this(obj.getAntiguidadeRelativa(),
			obj.getTitulacao(),
			obj.getNomeCompleto(),
			obj.getCpf(),
			new UserImageRecord(obj.getUserImage()),
			obj.getPosto(),
			obj.getQuadro(),
			obj.getEspecialidade(),
			obj.getNomeGuerra(),
			obj.getSexo(),
			ConvertDateToMillis.converter(obj.getUltimaPromocao())+"",
			obj.getEmail(),
			obj.isPttc(),
			obj.getSaram(),
			obj.getIdentidade(),
			obj.getCelular(),
			ConvertDateToMillis.converter(obj.getDataPraca())+"",
			obj.getSenha(),
			obj.getDocuments(),
			ConvertDateToMillis.converter(obj.getDataNascimento())+"",
			obj.getRole(),
			ConvertDateToMillis.converter(obj.getProximaPromocao())+"",
			obj.getObservacoes(),
			obj.getForcaSingular(),
			obj.getId(),
			obj.isAtivo());
	}
}