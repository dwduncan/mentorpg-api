package mil.decea.mentorpgapi.domain.academic;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;

import java.util.List;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
public record AreaDePesquisaRecord(
List<?> linhasDePesquisa,
UserRecord coordenadorDaArea,
EmbeddedExternalDataRecord imagemAreaDePesquisaRecord,
@NotNull(message="Obrigat\u00f3rio informar a sigla", payload={}, groups={})
String sigla,
boolean encerrado,
String conceito,
String nomeExtenso,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public AreaDePesquisaRecord(AreaDePesquisa obj) {
		this(obj.getLinhasDePesquisa(),
			new UserRecord(obj.getCoordenadorDaArea()),
			new EmbeddedExternalDataRecord(obj.getImagemAreaDePesquisa()),
			obj.getSigla(),
			obj.isEncerrado(),
			obj.getConceito(),
			obj.getNomeExtenso(),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}
}