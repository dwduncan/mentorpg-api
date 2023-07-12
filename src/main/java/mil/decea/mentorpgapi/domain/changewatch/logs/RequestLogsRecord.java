package mil.decea.mentorpgapi.domain.changewatch.logs;

import mil.decea.mentorpgapi.util.datageneration.OptionalsRecordField;

public record RequestLogsRecord (
    String parentClass,
    Long parentId,
    @OptionalsRecordField  String objectClass,
    @OptionalsRecordField Long objectId,
    int page,
    int size){

}
