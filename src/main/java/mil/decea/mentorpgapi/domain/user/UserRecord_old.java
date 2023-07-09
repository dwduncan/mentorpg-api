package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;

import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;
import java.util.List;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
public record UserRecord_old(
int antiguidadeRelativa,
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
String cpf,
EmbeddedExternalDataRecord userImageRecord,
String quadro,
boolean pttc,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
Sexo sexo,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String especialidade,
Posto posto,
List<UserDocumentRecord>  documents,
String nomeQualificado,
String celular,
String identidade,
Titulacao titulacao,
String ultimaPromocao,
ForcaSingular forcaSingular,
String role,
String email,
String saram,
String dataPraca,
String proximaPromocao,
String observacoes,
String dataNascimento,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public UserRecord_old(User obj) {
		this(obj.getAntiguidadeRelativa(),
			obj.getCpf(),
			new EmbeddedExternalDataRecord(obj.getUserImage()),
			obj.getQuadro(),
			obj.isPttc(),
			obj.getNomeCompleto(),
			obj.getSexo(),
			obj.getNomeGuerra(),
			obj.getEspecialidade(),
			obj.getPosto(),
			obj.getDocuments().stream().map(UserDocumentRecord::new).toList(),
			obj.getNomeQualificado(),
			obj.getCelular(),
			obj.getIdentidade(),
			obj.getTitulacao(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao())+"",
			obj.getForcaSingular(),
			obj.getRole(),
			obj.getEmail(),
			obj.getSaram(),
			DateTimeAPIHandler.converter(obj.getDataPraca())+"",
			DateTimeAPIHandler.converter(obj.getProximaPromocao())+"",
			obj.getObservacoes(),
			DateTimeAPIHandler.converter(obj.getDataNascimento())+"",
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}

	public UserRecord_old(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {
		this(0,
			cpf,
			null,
			quadro,
			pttc,
			nomeCompleto,
			sexo,
			nomeGuerra,
			especialidade,
			posto,
			null,
			null,
			null,
			null,
			titulacao,
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

	public UserRecord_old(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {
		this(0,
			cpf,
			null,
			quadro,
			false,
			nomeCompleto,
			null,
			nomeGuerra,
			especialidade,
			posto,
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
			null,
			null,
			null,
			null,
			id,
			ativo,
			null);
	}
}