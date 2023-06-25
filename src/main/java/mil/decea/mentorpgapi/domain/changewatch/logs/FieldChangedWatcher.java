package mil.decea.mentorpgapi.domain.changewatch.logs;

@SuppressWarnings({"unused"})
public interface FieldChangedWatcher {
    Long getObjectId();
    String getObjectClass();
    String getPreviousValue();
    Long getParentId();
    String getParentClass();
    boolean isNeverExpires();
    void setObjectId(Long objectId);

    void setObjectClass(String objectClass);

    void setPreviousValue(String previousValueOrMessage);

    void setParentId(Long objectOwnerId);

    void setParentClass(String objectOwnerClass);

    void setNeverExpires(boolean neverExpires);
}
