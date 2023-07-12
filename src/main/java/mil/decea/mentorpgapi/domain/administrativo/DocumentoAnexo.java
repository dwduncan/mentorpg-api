package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

@Table(name = "documentosanexos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class DocumentoAnexo extends ExternalDataEntity  {

    @Override
    @MethodDefaultValue(fieldName = "bucket",defaultValue = "\"documentosanexos\"")
    public String getBucket() {
        return "documentosanexos";
    }

    @Override
    @MethodDefaultValue(fieldName = "storageDestinationPath",
            defaultValue = "\"id_\" + getId() +\"/\"++obj.getNomeArquivo()")
    public String getStorageDestinationPath() {
        return "id_" + getId() + "/" + getNomeArquivo();
    }
    @Override
    @NotForRecordField
    public String getPreviousStorageDestinationPath() {
        return "id_" + getId() + "/" + previousFileName;
    }
    @Override
    public DocumentoAnexo getExternalData() {
        return this;
    }

    @Override
    public String getEntityDescriptor() {
        return "DocumentoAnexo: " + getId();
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }

}
