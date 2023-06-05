package mil.decea.mentorpgapi.domain.daoservices.minio;

public interface MinioStorage<T extends ExternalData> {

    String getBucket();
    String getStorageDestinationPath();

    T getExternalData();

}
