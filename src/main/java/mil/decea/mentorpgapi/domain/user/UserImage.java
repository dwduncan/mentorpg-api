package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.externaldataio.AbstractExternalData;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserImage extends AbstractExternalData {

    @NotForRecordField
    public List<FieldChangedWatcher> onValuesUpdated(UserImageRecord rec) {
        this.setBase64Data(rec.base64Data());
        this.setArquivoUrl(rec.arquivoUrl());
        this.setFormato(rec.formato());
        this.setNomeArquivo(rec.nomeArquivo());
        this.setDataHoraUpload(DateTimeAPIHandler.converterStringDate(rec.dataHoraUpload()));
        this.setTamanho(rec.tamanho());
        return new ArrayList<>();
    }

    public void copyFields(UserImage previousEntity) {
        this.setBase64Data(previousEntity.getBase64Data());
        this.setArquivoUrl(previousEntity.getArquivoUrl());
        this.setFormato(previousEntity.getFormato());
        this.setNomeArquivo(previousEntity.getNomeArquivo());
        this.setDataHoraUpload(previousEntity.getDataHoraUpload());
        this.setTamanho(previousEntity.getTamanho());
    }


    public Long getId(){
        return null;
    }

}
