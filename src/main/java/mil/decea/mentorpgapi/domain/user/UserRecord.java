package mil.decea.mentorpgapi.domain.user;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;

public record UserRecord(
String nomeQualificado,
String dataPraca,
String proximaPromocao,
String observacoes,
Sexo sexo,
String role,
String especialidade,
ForcaSingular forcaSingular,
List<UserDocumentRecord>  documents,
Posto posto,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String email,
String identidade,
String dataNascimento,
String ultimaPromocao,
Titulacao titulacao,
String celular,
String saram,
String quadro,
EmbeddedExternalDataRecord userImageRecord,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
boolean pttc,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
int antiguidadeRelativa,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public UserRecord(User obj) {
		this(obj.getNomeQualificado(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			obj.getObservacoes(),
			obj.getSexo(),
			obj.getRole(),
			obj.getEspecialidade(),
			obj.getForcaSingular(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getPosto(),
			obj.getNomeGuerra(),
			obj.getEmail(),
			obj.getIdentidade(),
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			obj.getTitulacao(),
			obj.getCelular(),
			obj.getSaram(),
			obj.getQuadro(),
			new EmbeddedExternalDataRecord(obj.getUserImage()),
			obj.getCpf(),
			obj.isPttc(),
			obj.getNomeCompleto(),
			obj.getAntiguidadeRelativa(),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}

	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(null,
			null,
			null,
			null,
			sexo,
			null,
			especialidade,
			null,
			null,
			posto,
			nomeGuerra,
			null,
			null,
			null,
			null,
			titulacao,
			null,
			null,
			quadro,
			null,
			cpf,
			pttc,
			nomeCompleto,
			0,
			id,
			ativo,
			null);
	}

	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(null,
			null,
			null,
			null,
			null,
			null,
			especialidade,
			null,
			null,
			posto,
			nomeGuerra,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			quadro,
			null,
			cpf,
			false,
			nomeCompleto,
			0,
			id,
			ativo,
			null);
	}
}