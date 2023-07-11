package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.ExternalDataRecord;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.AppendFieldLabelDescriptor;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.IgnoreTrackChange;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackOnlySelectedFields;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedElementCollection;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.StatusDoc;
import mil.decea.mentorpgapi.domain.documents.DocumentType;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import mil.decea.mentorpgapi.domain.user.User;

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
