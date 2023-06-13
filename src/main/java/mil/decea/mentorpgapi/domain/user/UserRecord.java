package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.user.UserImageRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import java.lang.Long;

public record UserRecord(
int antiguidadeRelativa,
	UserImageRecord userImageRecord,
String quadro,
boolean pttc,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
String nomeQualificado,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
String email,
String dataPraca,
String dataNascimento,
String proximaPromocao,
String saram,
String ultimaPromocao,
String identidade,
ForcaSingular forcaSingular,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String observacoes,
Posto posto,
String role,
Sexo sexo,
String especialidade,
String celular,
	List<UserDocumentRecord>  documents,
Titulacao titulacao,
Long id,
boolean ativo) {
	public UserRecord(User obj) {
		this(obj.getAntiguidadeRelativa(),
			new UserImageRecord(obj.getUserImage()),
			obj.getQuadro(),
			obj.isPttc(),
			obj.getCpf(),
			obj.getNomeQualificado(),
			obj.getNomeCompleto(),
			obj.getEmail(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			obj.getSaram(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			obj.getIdentidade(),
			obj.getForcaSingular(),
			obj.getNomeGuerra(),
			obj.getObservacoes(),
			obj.getPosto(),
			obj.getRole(),
			obj.getSexo(),
			obj.getEspecialidade(),
			obj.getCelular(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getTitulacao(),
			obj.getId(),
			obj.isAtivo());
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(0,
			null,
			quadro,
			pttc,
			cpf,
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
			nomeGuerra,
			null,
			posto,
			null,
			sexo,
			especialidade,
			null,
			null,
			titulacao,
			id,
			ativo);
	}

	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(0,
			null,
			quadro,
			false,
			cpf,
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
			nomeGuerra,
			null,
			posto,
			null,
			null,
			especialidade,
			null,
			null,
			null,
			id,
			ativo);
	}
}