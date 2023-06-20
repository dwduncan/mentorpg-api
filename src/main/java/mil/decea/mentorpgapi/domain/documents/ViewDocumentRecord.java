package mil.decea.mentorpgapi.domain.documents;

public record ViewDocumentRecord(Long id,
                                 String url,
                                 int duracaoEmSegundos,
                                 String storageDestinationPath,
                                 String bucket,
                                 String nomeArquivo) {
}
