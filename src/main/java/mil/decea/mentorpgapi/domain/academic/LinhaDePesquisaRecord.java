package mil.decea.mentorpgapi.domain.academic;

import mil.decea.mentorpgapi.domain.IdentifiedRecord;

import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
public record LinhaDePesquisaRecord(
@NotNull(message="{NotNull.message}", payload={}, groups={})
String nome,
String conceito,
AreaDePesquisaRecord areaDePesquisa,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public LinhaDePesquisaRecord(LinhaDePesquisa obj) {
		this(obj.getNome(),
			obj.getConceito(),
			new AreaDePesquisaRecord(obj.getAreaDePesquisa()),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}
}