package mil.decea.mentorpgapi.domain.daoservices.minio;

import mil.decea.mentorpgapi.domain.ExternalDataRecord;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;

public interface ExternalData {
    String getFormato();

    String getNomeArquivo();

    java.time.LocalDateTime getLastUpdate();

    String getArquivoUrl();

    String getBase64Data();

    String getPreviousFileName();

    void setFormato(String formato);

    void setNomeArquivo(String nomeArquivo);

    void setLastUpdate(java.time.LocalDateTime dataHoraUpload);

    void setArquivoUrl(String arquivoUrl);

    void setBase64Data(String base64Data);

    void updateValues(ExternalDataRecord rec);

    @NotForRecordField
    default String[] getFileNamePrefixSuffix(){
        if (getNomeArquivo() != null && getNomeArquivo().contains(".")){
            return getNomeArquivo().split("\\.");
        }
        return null;
    };




}
