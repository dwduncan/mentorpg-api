package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserImage extends AbstractExternalData {

    public void setUserImage(UserImageRecord rec) {
        this.setFormato(rec.formato());
        this.setBase64Data(rec.base64Data());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setArquivoUrl(rec.arquivoUrl());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
    }

}
