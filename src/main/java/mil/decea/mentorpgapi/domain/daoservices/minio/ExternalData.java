package mil.decea.mentorpgapi.domain.daoservices.minio;

import mil.decea.mentorpgapi.domain.NotForRecordField;

import java.io.Serializable;

public interface ExternalData extends Serializable {
    String getFormato();

    String getNomeArquivo();

    java.time.LocalDateTime getDataHoraUpload();

    String getArquivoUrl();

    String getBase64Data();

    void setFormato(String formato);

    void setNomeArquivo(String nomeArquivo);

    void setDataHoraUpload(java.time.LocalDateTime dataHoraUpload);

    void setArquivoUrl(String arquivoUrl);

    void setBase64Data(String base64Data);

    @NotForRecordField
    default String[] getFileNamePrefixSuffix(){
        if (getNomeArquivo() != null && getNomeArquivo().contains(".")){
            return getNomeArquivo().split("\\.");
        }
        return null;
    };



}