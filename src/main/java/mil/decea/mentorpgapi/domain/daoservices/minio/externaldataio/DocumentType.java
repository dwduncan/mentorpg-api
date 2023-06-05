package mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.BaseEntity;

@Entity
@Table(schema = "mentorpgapi", name = "documenttype")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentType extends BaseEntity implements Comparable<DocumentType> {

    private String tipo;
    @Override
    public String toString() {
        return tipo;
    }

    @Override
    public int compareTo(DocumentType o) {
        return getTipo().compareToIgnoreCase(o.getTipo());
    }


}
