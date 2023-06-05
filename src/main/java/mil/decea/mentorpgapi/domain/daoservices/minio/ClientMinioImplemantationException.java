package mil.decea.mentorpgapi.domain.daoservices.minio;

public class ClientMinioImplemantationException extends Exception{

    private final Exception originalException;

    public ClientMinioImplemantationException(Exception originalException) {
        this.originalException = originalException;
    }

    @Override
    public synchronized Throwable getCause() {
        return originalException.getCause();
    }

    @Override
    public String getMessage() {
        return originalException.getMessage();
    }

    @Override
    public StackTraceElement[] getStackTrace() {
        return originalException.getStackTrace();
    }

    @Override
    public String getLocalizedMessage() {
        return originalException.getLocalizedMessage();
    }
}
