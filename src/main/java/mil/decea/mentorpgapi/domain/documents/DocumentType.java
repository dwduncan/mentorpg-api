package mil.decea.mentorpgapi.domain.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.EntityDTOAdapter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackOnlySelectedFields;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;

@Entity
@Table(schema = "mentorpgapi", name = "documenttype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentType extends SequenceIdEntity implements Comparable<DocumentType> {

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
    public DocumentType(DocumentTypeRecord rec) {
        this.setTipo(rec.tipo());
        this.setId(rec.id());
        this.setAtivo(rec.ativo());
    }
    @Override
    public String getEntityDescriptor() {
        return getTipo();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
