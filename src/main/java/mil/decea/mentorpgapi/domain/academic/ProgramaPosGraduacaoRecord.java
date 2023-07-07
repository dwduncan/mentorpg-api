package mil.decea.mentorpgapi.domain.academic;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.academic.AreaDeConcentracaoRecord;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record ProgramaPosGraduacaoRecord(
List<AreaDeConcentracaoRecord>  areasConcentracao,
@NotNull(message="{NotNull.message}", payload={}, groups={})
String nome,
String definicao,
String fatoresCondicionantes,
UserRecord coordenador,
@NotNull(message="Obrigat\u00f3rio informar a sigla", payload={}, groups={})
String sigla,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public ProgramaPosGraduacaoRecord(ProgramaPosGraduacao obj) {
		this(obj.getAreasConcentracao().stream().map(AreaDeConcentracaoRecord::new).toList(),
			obj.getNome(),
			obj.getDefinicao(),
			obj.getFatoresCondicionantes(),
			new UserRecord(obj.getCoordenador()),
			obj.getSigla(),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}
}