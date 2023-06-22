package mil.decea.mentorpgapi.domain.documents;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackChange;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;

import java.util.List;

@Entity
@Table(schema = "mentorpgapi", name = "documenttype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TrackChange
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
    public List<FieldChangedWatcher> onValuesUpdated(IdentifiedRecord incomingData) {

        DocumentTypeRecord rec = (DocumentTypeRecord) incomingData;

        List<FieldChangedWatcher> changes = new ObjectChangesChecker<>(this, rec).getChangesList();

        this.setTipo(rec.tipo());
        this.setId(rec.id());
        this.setAtivo(rec.ativo());

        return changes;
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
