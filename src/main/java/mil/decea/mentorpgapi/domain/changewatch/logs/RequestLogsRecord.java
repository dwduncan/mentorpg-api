package mil.decea.mentorpgapi.domain.changewatch.logs;

import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.OptionalsRecordField;

public record RequestLogsRecord (
    String parentClass,
    Long parentId,
    @OptionalsRecordField  String objectClass,
    @OptionalsRecordField Long objectId,
    int page,
    int size){

}
