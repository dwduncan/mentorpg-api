package mil.decea.mentorpgapi.domain;

public interface ExternalDataRecord extends IdentifiedRecord{

    String previousFileName();
    String nomeArquivo();
    String lastUpdate();
    String base64Data();
    String arquivoUrl();
    String formato();
    long tamanho();

}
