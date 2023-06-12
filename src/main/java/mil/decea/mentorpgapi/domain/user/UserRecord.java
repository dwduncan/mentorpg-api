package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.user.UserImageRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import java.lang.Long;

public record UserRecord(
String quadro,
	UserImageRecord userImageRecord,
boolean pttc,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
int antiguidadeRelativa,
String dataPraca,
String saram,
String proximaPromocao,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
Titulacao titulacao,
String ultimaPromocao,
Sexo sexo,
String email,
String dataNascimento,
ForcaSingular forcaSingular,
String role,
String nomeQualificado,
String especialidade,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String celular,
String identidade,
Posto posto,
String observacoes,
	List<UserDocumentRecord>  documents,
Long id,
boolean ativo) {
	public UserRecord(User obj) {
		this(obj.getQuadro(),
			new UserImageRecord(obj.getUserImage()),
			obj.isPttc(),
			obj.getCpf(),
			obj.getAntiguidadeRelativa(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			obj.getSaram(),
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			obj.getNomeCompleto(),
			obj.getTitulacao(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			obj.getSexo(),
			obj.getEmail(),
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			obj.getForcaSingular(),
			obj.getRole(),
			obj.getNomeQualificado(),
			obj.getEspecialidade(),
			obj.getNomeGuerra(),
			obj.getCelular(),
			obj.getIdentidade(),
			obj.getPosto(),
			obj.getObservacoes(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getId(),
			obj.isAtivo());
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(quadro,
			null,
			pttc,
			cpf,
			0,
			null,
			null,
			null,
			nomeCompleto,
			titulacao,
			null,
			sexo,
			null,
			null,
			null,
			null,
			null,
			especialidade,
			nomeGuerra,
			null,
			null,
			posto,
			null,
			null,
			id,
			ativo);
	}

	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(quadro,
			null,
			false,
			cpf,
			0,
			null,
			null,
			null,
			nomeCompleto,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			especialidade,
			nomeGuerra,
			null,
			null,
			posto,
			null,
			null,
			id,
			ativo);
	}
}