package mil.decea.mentorpgapi.domain.documents;


import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;import java.lang.Long;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;


@NoArgsConstructor
@Service
public class DocumentTypeAdapter extends AbstractEntityDTOAdapter<DocumentType, DocumentTypeRecord> {


	@Override
	public DocumentTypeRecord generateRecord() { return new DocumentTypeRecord(getEntity());}

	public DocumentType updateEntity() {
		getEntity().setTipo(getIdentifiedRecord().tipo());
		getEntity().setId(getIdentifiedRecord().id());
		getEntity().setAtivo(getIdentifiedRecord().ativo());
		getEntity().setLastUpdate(DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().lastUpdate()));
		return getEntity();
	}
}