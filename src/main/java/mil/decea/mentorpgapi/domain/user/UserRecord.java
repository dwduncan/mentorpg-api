package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import java.lang.Long;

public record UserRecord(
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
EmbeddedExternalDataRecord userImageRecord,
boolean pttc,
String quadro,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
String nomeQualificado,
	List<UserDocumentRecord>  documents,
int antiguidadeRelativa,
ForcaSingular forcaSingular,
Titulacao titulacao,
Posto posto,
Sexo sexo,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String email,
String especialidade,
String identidade,
String saram,
String role,
String dataNascimento,
String celular,
String observacoes,
String ultimaPromocao,
String proximaPromocao,
String dataPraca,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public UserRecord(User obj) {
		this(obj.getCpf(),
			new EmbeddedExternalDataRecord(obj.getUserImage()),
			obj.isPttc(),
			obj.getQuadro(),
			obj.getNomeCompleto(),
			obj.getNomeQualificado(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getAntiguidadeRelativa(),
			obj.getForcaSingular(),
			obj.getTitulacao(),
			obj.getPosto(),
			obj.getSexo(),
			obj.getNomeGuerra(),
			obj.getEmail(),
			obj.getEspecialidade(),
			obj.getIdentidade(),
			obj.getSaram(),
			obj.getRole(),
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			obj.getCelular(),
			obj.getObservacoes(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(cpf,
			null,
			pttc,
			quadro,
			nomeCompleto,
			null,
			null,
			0,
			null,
			titulacao,
			posto,
			sexo,
			nomeGuerra,
			null,
			especialidade,
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
		this(cpf,
			null,
			false,
			quadro,
			nomeCompleto,
			null,
			null,
			0,
			null,
			null,
			posto,
			null,
			nomeGuerra,
			null,
			especialidade,
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