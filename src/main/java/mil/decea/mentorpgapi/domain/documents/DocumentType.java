package mil.decea.mentorpgapi.domain.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.changewatch.TrackChange;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;

@Entity
@Table(schema = "mentorpgapi", name = "documenttype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TrackChange(recordClass = DocumentTypeRecord.class)
public class DocumentType extends BaseEntity<DocumentType,DocumentTypeRecord> implements Comparable<DocumentType> {

    private String tipo;
    @Override
    public String toString() {
        return tipo;
    }

    @Override
    public int compareTo(DocumentType o) {
        return getTipo().compareToIgnoreCase(o.getTipo());
    }
    @NotForRecordField
    public void updateValues(DocumentTypeRecord rec) {
        this.setTipo(rec.tipo());
        this.setId(rec.id());
        this.setAtivo(rec.ativo());
    }


    @NotForRecordField
    public DocumentType(DocumentTypeRecord rec) {
        this.setTipo(rec.tipo());
        this.setId(rec.id());
        this.setAtivo(rec.ativo());
    }

    @Override
    public String getEntityDescriptor() {
        return getTipo();
    }
}
