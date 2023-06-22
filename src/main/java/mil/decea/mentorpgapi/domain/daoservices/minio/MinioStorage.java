package mil.decea.mentorpgapi.domain.daoservices.minio;

import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;

import java.util.Objects;

public interface MinioStorage {
    String getBucket();

    String getStorageDestinationPath();
    ExternalData getExternalData();
    String getPreviousStorageDestinationPath();

    /**
     *
     * @return null
     */
    default boolean hasPreviousStorageDestinationPath(){
        return  (getExternalData().getPreviousFileName() != null && !getExternalData().getPreviousFileName().isBlank()
                && !Objects.equals(getExternalData().getNomeArquivo(), getExternalData().getPreviousFileName()));
    }
}
