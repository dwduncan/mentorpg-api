package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record DocumentTypeRecord(
String tipo,
Long id,
boolean ativo,
String lastUpdate) implements IdentifiedRecord {
	public DocumentTypeRecord(DocumentType obj) {
		this(obj.getTipo(),
			obj.getId(),
			obj.isAtivo(),
			DateTimeAPIHandler.converter(obj.getLastUpdate())+"");
	}

	public DocumentTypeRecord(String tipo) {
		this(tipo,
			null,
			false,
			null);
	}
}