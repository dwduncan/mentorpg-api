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
String quadro,
	UserImageRecord userImageRecord,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
boolean pttc,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
	List<UserDocumentRecord>  documents,
Posto posto,
String nomeQualificado,
Sexo sexo,
ForcaSingular forcaSingular,
Titulacao titulacao,
String especialidade,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String ultimaPromocao,
String proximaPromocao,
String observacoes,
String dataPraca,
String email,
String identidade,
String dataNascimento,
String role,
String saram,
String celular,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public UserRecord(User obj) {
		this(obj.getAntiguidadeRelativa(),
			obj.getQuadro(),
			new UserImageRecord(obj.getUserImage()),
			obj.getCpf(),
			obj.isPttc(),
			obj.getNomeCompleto(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getPosto(),
			obj.getNomeQualificado(),
			obj.getSexo(),
			obj.getForcaSingular(),
			obj.getTitulacao(),
			obj.getEspecialidade(),
			obj.getNomeGuerra(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			obj.getObservacoes(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			obj.getEmail(),
			obj.getIdentidade(),
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			obj.getRole(),
			obj.getSaram(),
			obj.getCelular(),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(0,
			quadro,
			null,
			cpf,
			pttc,
			nomeCompleto,
			null,
			posto,
			null,
			sexo,
			null,
			titulacao,
			especialidade,
			nomeGuerra,
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
			ativo,
			null);
	}

	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(0,
			quadro,
			null,
			cpf,
			false,
			nomeCompleto,
			null,
			posto,
			null,
			null,
			null,
			null,
			especialidade,
			nomeGuerra,
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
			ativo,
			null);
	}
}