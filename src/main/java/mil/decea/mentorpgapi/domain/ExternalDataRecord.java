package mil.decea.mentorpgapi.domain;

public interface ExternalDataRecord extends IdentifiedRecord{

    String previousFileName();
    String nomeArquivo();
    String dataHoraUpload();
    String base64Data();
    String arquivoUrl();
    String formato();
    long tamanho();

}
