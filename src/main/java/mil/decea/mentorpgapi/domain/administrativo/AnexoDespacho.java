package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.util.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.ExternalDataEntity;

@Table(name = "anexosdespachos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnexoDespacho extends ExternalDataEntity  {

    @ManyToOne(fetch = FetchType.LAZY)
    protected Despacho despacho;

    @Override
    @MethodDefaultValue(fieldName = "bucket",defaultValue = "\"anexosdespachos\"")
    public String getBucket() {
        return "anexosdespachos";
    }

    @Override
    @MethodDefaultValue(fieldName = "storageDestinationPath",
            defaultValue = "\"despacho_\" + getDespacho().getId() +\"/\" + getId() +\"_\" + obj.getNomeArquivo()")
    public String getStorageDestinationPath() {
        return "despacho_" + getDespacho().getId() + "/" + getId() + "_" + getNomeArquivo();
    }
    @Override
    @NotForRecordField
    public String getPreviousStorageDestinationPath() {
        return "despacho_" + getDespacho().getId() + "/" + getId() + "_" + previousFileName;
    }
    @Override
    public AnexoDespacho getExternalData() {
        return this;
    }

    @Override
    public String getEntityDescriptor() {
        return " Anexo Despacho " + despacho.getOcorrencia();
    }

    @Override
    public TrackedEntity getParentObject() {
        return despacho;
    }

}
