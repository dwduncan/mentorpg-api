package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackedByStringComparison;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@TrackedByStringComparison(recordFieldToCompare = "nomeArquivo")
public class UserImage extends AbstractExternalData {

    @Transient
    protected Long id;

    @NotForRecordField
    public void updateValues(UserImageRecord rec) {
        this.setBase64Data(rec.base64Data());
        this.setArquivoUrl(rec.arquivoUrl());
        this.setFormato(rec.formato());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setTamanho(rec.tamanho());
    }

    public void copyFields(UserImage previousEntity) {
        this.setBase64Data(previousEntity.getBase64Data());
        this.setArquivoUrl(previousEntity.getArquivoUrl());
        this.setFormato(previousEntity.getFormato());
        this.setNomeArquivo(previousEntity.getNomeArquivo());
        this.setDataHoraUpload(previousEntity.getDataHoraUpload());
        this.setTamanho(previousEntity.getTamanho());
    }


    @Override
    public String toString() {
        return getNomeArquivo();
    }
}
