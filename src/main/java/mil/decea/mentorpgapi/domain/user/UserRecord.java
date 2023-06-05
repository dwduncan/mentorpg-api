package mil.decea.mentorpgapi.domain.user;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

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
			obj.getUserImage() == null ? null : new UserImageRecord(obj.getUserImage()),
			obj.getPosto(),
			obj.getQuadro(),
			obj.getEspecialidade(),
			obj.getNomeGuerra(),
			obj.getSexo(),
			obj.getUltimaPromocao() == null ? "" : DateTimeAPIHandler.converter(obj.getUltimaPromocao()) + "",
			obj.getEmail(),
			obj.isPttc(),
			obj.getSaram(),
			obj.getIdentidade(),
			obj.getCelular(),
			obj.getDataPraca() == null ? "" : DateTimeAPIHandler.converter(obj.getDataPraca()) + "",
			obj.getDocuments(),
			obj.getDataNascimento() == null ? "" : DateTimeAPIHandler.converter(obj.getDataNascimento()) + "",
			obj.getRole(),
			obj.getProximaPromocao() == null ? "" : DateTimeAPIHandler.converter(obj.getProximaPromocao()) + "",
			obj.getObservacoes(),
			obj.getForcaSingular(),
			obj.getId(),
			obj.isAtivo());
	}



	public UserRecord(Long id,
					  String cpf,
					  boolean ativo,
					  Posto posto,
					  String quadro,
					  String especialidade,
					  String nomeGuerra,
					  String nomeCompleto,
					  UserImage userImage) {
		this(0,
				null,
				nomeCompleto,
				cpf,
				userImage == null ? null : new UserImageRecord(userImage),
				posto,
				quadro,
				especialidade,
				nomeGuerra,
				null,
				null,
				null,
				false,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				null,
				id,
				ativo);
	}
	public UserRecord(Long id,
					  String cpf,
					  boolean ativo,
					  Posto posto,
					  String quadro,
					  String especialidade,
					  String nomeGuerra,
					  String nomeCompleto) {
		this(id,
				cpf,
				ativo,
				posto,
				quadro,
				especialidade,
				nomeGuerra,
				nomeCompleto,
				null);
	}
}