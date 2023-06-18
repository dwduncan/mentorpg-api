package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import mil.decea.mentorpgapi.domain.user.UserImageRecord;
import java.lang.Long;

public record UserRecord(
Titulacao titulacao,
Posto posto,
String especialidade,
	List<UserDocumentRecord>  documents,
String nomeQualificado,
ForcaSingular forcaSingular,
String dataNascimento,
String observacoes,
String email,
String saram,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
String role,
Sexo sexo,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String proximaPromocao,
String ultimaPromocao,
String identidade,
String dataPraca,
String celular,
int antiguidadeRelativa,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
	UserImageRecord userImageRecord,
boolean pttc,
String quadro,
Long id,
boolean ativo) {
	public UserRecord(User obj) {
		this(obj.getTitulacao(),
			obj.getPosto(),
			obj.getEspecialidade(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getNomeQualificado(),
			obj.getForcaSingular(),
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			obj.getObservacoes(),
			obj.getEmail(),
			obj.getSaram(),
			obj.getNomeCompleto(),
			obj.getRole(),
			obj.getSexo(),
			obj.getNomeGuerra(),
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			obj.getIdentidade(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			obj.getCelular(),
			obj.getAntiguidadeRelativa(),
			obj.getCpf(),
			new UserImageRecord(obj.getUserImage()),
			obj.isPttc(),
			obj.getQuadro(),
			obj.getId(),
			obj.isAtivo());
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(titulacao,
			posto,
			especialidade,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			nomeCompleto,
			null,
			sexo,
			nomeGuerra,
			null,
			null,
			null,
			null,
			null,
			0,
			cpf,
			null,
			pttc,
			quadro,
			id,
			ativo);
	}

	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(null,
			posto,
			especialidade,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			nomeCompleto,
			null,
			null,
			nomeGuerra,
			null,
			null,
			null,
			null,
			null,
			0,
			cpf,
			null,
			false,
			quadro,
			id,
			ativo);
	}
}