package mil.decea.mentorpgapi.domain;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedByStringComparison;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@TrackedByStringComparison(recordFieldToCompare = "nomeArquivo")
public abstract class EmbeddedExternalData extends AbstractExternalData implements TrackedEntity{

    @Transient
    protected Long id;
    @Override
    public String toString() {
        return getNomeArquivo();
    }


    @Override
    public String getEntityDescriptor() {
        return getNomeArquivo();
    }

    @NotForRecordField
    public void updateValues(EmbeddedExternalDataRecord rec) {
        this.setBase64Data(rec.base64Data());
        this.setArquivoUrl(rec.arquivoUrl());
        this.setFormato(rec.formato());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setLastUpdate(DateTimeAPIHandler.converterStringDate(rec.lastUpdate()));
        this.setTamanho(rec.tamanho());
    }

}
