package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserImage extends AbstractExternalData {

    @NotForRecordField
    public void setUserImage(UserImageRecord rec) {
        this.setBase64Data(rec.base64Data());
        this.setArquivoUrl(rec.arquivoUrl());
        this.setFormato(rec.formato());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setTamanho(rec.tamanho());
    }

}
