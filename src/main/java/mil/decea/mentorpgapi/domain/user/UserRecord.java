package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;


public record UserRecord(
int antiguidadeRelativa,
String quadro,
boolean pttc,
@NotNull(message="Informe o nome completo", payload={}, groups={})
String nomeCompleto,
String nomeQualificado,
String especialidade,
String celular,
ForcaSingular forcaSingular,
String dataNascimento,
String identidade,
String role,
Sexo sexo,
Posto posto,
String proximaPromocao,
@NotNull(message="Informe o nome de guerra", payload={}, groups={})
String nomeGuerra,
String dataPraca,
String email,
Titulacao titulacao,
String ultimaPromocao,
String saram,
String observacoes,
@NotNull(message="Informe um CPF v\u00e1lido", payload={}, groups={})
@mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf(message="Cpf inv\u00e1lido", payload={}, groups={})
String cpf,
EmbeddedExternalDataRecord userImage,
Long id,
String lastUpdate,
boolean ativo) implements IdentifiedRecord {

	public UserRecord(User obj) {
		this(obj.getAntiguidadeRelativa(),
			obj.getQuadro(),
			obj.isPttc(),
			obj.getNomeCompleto(),
			obj.getNomeQualificado(),
			obj.getEspecialidade(),
			obj.getCelular(),
			obj.getForcaSingular(),
			DateTimeAPIHandler.converter(obj.getDataNascimento()),
			obj.getIdentidade(),
			obj.getRole(),
			obj.getSexo(),
			obj.getPosto(),
			DateTimeAPIHandler.converter(obj.getProximaPromocao()),
			obj.getNomeGuerra(),
			DateTimeAPIHandler.converter(obj.getDataPraca()),
			obj.getEmail(),
			obj.getTitulacao(),
			DateTimeAPIHandler.converter(obj.getUltimaPromocao()),
			obj.getSaram(),
			obj.getObservacoes(),
			obj.getCpf(),
			new EmbeddedExternalDataRecord(obj.getUserImage()),
			obj.getId(),
			DateTimeAPIHandler.converter(obj.getLastUpdate()),
			obj.isAtivo()
		);
	}
	public UserRecord(Long id, String cpf, boolean ativo, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto) {

		this(0,
			quadro,
			false,
			nomeCompleto,
			null,
			especialidade,
			null,
			null,
			null,
			null,
			null,
			null,
			posto,
			null,
			nomeGuerra,
			null,
			null,
			null,
			null,
			null,
			null,
			cpf,
			null,
			id,
			null,
			ativo
		);
	}
	public UserRecord(Long id, boolean ativo, String cpf, Titulacao titulacao, Posto posto, String quadro, String especialidade, String nomeGuerra, String nomeCompleto, Sexo sexo, boolean pttc) {

		this(0,
			quadro,
			pttc,
			nomeCompleto,
			null,
			especialidade,
			null,
			null,
			null,
			null,
			null,
			sexo,
			posto,
			null,
			nomeGuerra,
			null,
			null,
			titulacao,
			null,
			null,
			null,
			cpf,
			null,
			id,
			null,
			ativo
		);
	}

}