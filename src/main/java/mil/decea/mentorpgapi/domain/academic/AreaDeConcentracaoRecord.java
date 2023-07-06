package mil.decea.mentorpgapi.domain.academic;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record AreaDeConcentracaoRecord(
UserRecord representanteDaArea,
@NotNull(message="Obrigat\u00f3rio informar a sigla", payload={}, groups={})
String sigla,
String nome,
@NotNull(message="{NotNull.message}", payload={}, groups={})
ProgramaPosGraduacao programa,
String definicao,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public AreaDeConcentracaoRecord(AreaDeConcentracao obj) {
		this(obj.getRepresentanteDaArea(),
			obj.getSigla(),
			obj.getNome(),
			obj.getPrograma(),
			obj.getDefinicao(),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}
}