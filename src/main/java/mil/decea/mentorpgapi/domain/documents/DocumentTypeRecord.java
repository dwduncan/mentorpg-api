package mil.decea.mentorpgapi.domain.documents;
import java.lang.Long;

public record DocumentTypeRecord(
String tipo,
Long id,
boolean ativo) {
	public DocumentTypeRecord(DocumentType obj) {
		this(obj.getTipo(),
			obj.getId(),
			obj.isAtivo());
	}

	public DocumentTypeRecord(String tipo) {
		this(tipo,
			null,
			false);
	}
}