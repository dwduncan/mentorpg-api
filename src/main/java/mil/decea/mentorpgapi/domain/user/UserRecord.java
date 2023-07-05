package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.UserImageRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import java.lang.Long;

public record UserRecord(
int antiguidadeRelativa,
	UserImageRecord userImageRecord,
String quadro,
boolean pttc,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
Titulacao titulacao,
String role,
String email,
String identidade,
String especialidade,
String celular,
	List<UserDocumentRecord>  documents,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
ForcaSingular forcaSingular,
String ultimaPromocao,
Posto posto,
String nomeQualificado,
Sexo sexo,
String dataPraca,
String observacoes,
String saram,
String proximaPromocao,
String dataNascimento,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public UserRecord(User obj) {
		this(obj.getAntiguidadeRelativa(),
			new UserImageRecord(obj.getUserImage()),
			obj.getQuadro(),
			obj.isPttc(),
			obj.getCpf(),
			obj.getNomeCompleto(),
			obj.getTitulacao(),
			obj.getRole(),
			obj.getEmail(),
			obj.getIdentidade(),
			obj.getEspecialidade(),
			obj.getCelular(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getNomeGuerra(),
			obj.getForcaSingular(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			obj.getPosto(),
			obj.getNomeQualificado(),
			obj.getSexo(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			obj.getObservacoes(),
			obj.getSaram(),
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(0,
			null,
			quadro,
			pttc,
			cpf,
			nomeCompleto,
			titulacao,
			null,
			null,
			null,
			especialidade,
			null,
			null,
			nomeGuerra,
			null,
			null,
			posto,
			null,
			sexo,
			null,
			null,
			null,
			null,
			null,
			id,
			ativo,
			null);
	}

	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(0,
			null,
			quadro,
			false,
			cpf,
			nomeCompleto,
			null,
			null,
			null,
			null,
			especialidade,
			null,
			null,
			nomeGuerra,
			null,
			null,
			posto,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			id,
			ativo,
			null);
	}
}