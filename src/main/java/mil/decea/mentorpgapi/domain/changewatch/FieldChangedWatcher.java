package mil.decea.mentorpgapi.domain.changewatch;

public interface FieldChangedWatcher {
    Long getObjectId();

    String getObjectClass();

    String getFieldName();

    String getFieldType();

    String getBefore();

    String getAfterOrMessage();

    Long getObjectOwnerId();

    String getObjectOwnerClass();

    void setObjectId(Long objectId);

    void setObjectClass(String objectClass);

    void setFieldName(String fieldName);

    void setFieldType(String fieldType);

    void setBefore(String before);

    void setAfterOrMessage(String afterOrMessage);

    void setObjectOwnerId(Long objectOwnerId);

    void setObjectOwnerClass(String objectOwnerClass);
}
