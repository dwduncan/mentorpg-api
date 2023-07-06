package mil.decea.mentorpgapi.domain.academic;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.User;
import jakarta.validation.constraints.NotNull;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record AreaDeConcentracaoRecord(
User representanteDaArea,
@NotNull(message="Obrigat\u00f3rio informar a sigla", payload={}, groups={})
String sigla,
String nome,
@NotNull(message="{NotNull.message}", payload={}, groups={})
ProgramaPosGraduacao programa,
String definicao,
Long id,
String lastUpdate,
boolean ativo) implements IdentifiedRecord {
	public AreaDeConcentracaoRecord(AreaDeConcentracao obj) {
		this(obj.getRepresentanteDaArea(),
			obj.getSigla(),
			obj.getNome(),
			obj.getPrograma(),
			obj.getDefinicao(),
			obj.getId(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"",
			obj.isAtivo());
	}
}