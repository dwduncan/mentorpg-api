package mil.decea.mentorpgapi.domain.academic;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.user.UserRecord_old;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record AreaDeConcentracaoRecord(
UserRecord_old representanteDaArea,
@NotNull(message="Obrigat\u00f3rio informar a sigla", payload={}, groups={})
String sigla,
String definicao,
String nome,
ProgramaPosGraduacaoRecord programa,
Long id,
String lastUpdate,
boolean ativo) implements IdentifiedRecord {
	public AreaDeConcentracaoRecord(AreaDeConcentracao obj) {
		this(new UserRecord_old(obj.getRepresentanteDaArea()),
			obj.getSigla(),
			obj.getDefinicao(),
			obj.getNome(),
			new ProgramaPosGraduacaoRecord(obj.getPrograma()),
			obj.getId(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"",
			obj.isAtivo());
	}
}