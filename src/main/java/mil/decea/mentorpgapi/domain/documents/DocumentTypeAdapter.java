package mil.decea.mentorpgapi.domain.documents;

import mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;

public class DocumentTypeAdapter extends AbstractEntityDTOAdapter<DocumentType, DocumentTypeRecord> {

    public DocumentTypeAdapter() {
        super(null, null);
    }

    public DocumentTypeAdapter(DocumentType entity) {
        super(entity == null ? new DocumentType() : entity, null);
    }

    public DocumentTypeAdapter(DocumentType entity, DocumentTypeRecord indentifiedRecord) {
        super(entity, indentifiedRecord);
    }

    @Override
    public DocumentTypeRecord generateRecord() {
        return new DocumentTypeRecord(getEntity());
    }

    @Override
    public DocumentType updateEntity() {
        getEntity().setTipo(this.getIdentifiedRecord().tipo());
        getEntity().setId(this.getIdentifiedRecord().id());
        getEntity().setAtivo(this.getIdentifiedRecord().ativo());
        return getEntity();
    }
}
