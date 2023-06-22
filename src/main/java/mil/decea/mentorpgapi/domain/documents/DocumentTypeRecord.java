package mil.decea.mentorpgapi.domain.documents;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;

import java.lang.Long;

public record DocumentTypeRecord(
String tipo,
Long id,
boolean ativo) implements IdentifiedRecord {
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