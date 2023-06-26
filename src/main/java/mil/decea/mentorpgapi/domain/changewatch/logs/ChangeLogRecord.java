package mil.decea.mentorpgapi.domain.changewatch.logs;
import mil.decea.mentorpgapi.domain.IdentifiedRecord;
import java.lang.Long;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;

public record ChangeLogRecord(
Long id,
@NotNull(message="{NotNull.message}", payload={}, groups={})
String occurrenceTime,
boolean neverExpires,
@NotNull(message="{NotNull.message}", payload={}, groups={})
String responsableName,
@NotNull(message="{NotNull.message}", payload={}, groups={})
Long responsableId,
@NotNull(message="{NotNull.message}", payload={}, groups={})
Long parentId,
@NotNull(message="{NotNull.message}", payload={}, groups={})
String objectClass,
@NotNull(message="{NotNull.message}", payload={}, groups={})
String previousValue,
@NotNull(message="{NotNull.message}", payload={}, groups={})
Long objectId,
@NotNull(message="{NotNull.message}", payload={}, groups={})
String parentClass) implements IdentifiedRecord {
	public ChangeLogRecord(ChangeLog obj) {
		this(obj.getId(),
			DateTimeAPIHandler.converter(obj.getOccurrenceTime())+"",
			obj.isNeverExpires(),
			obj.getResponsableName(),
			obj.getResponsableId(),
			obj.getParentId(),
			obj.getObjectClass(),
			obj.getPreviousValue(),
			obj.getObjectId(),
			obj.getParentClass());
	}

	public ChangeLogRecord(Long id, String previousValue, Long parentId, String parentClass, Long objectId, String objectClass, Long responsableId, String responsableName, String occurrenceTime, boolean neverExpires) {
		this(id,
			occurrenceTime,
			neverExpires,
			responsableName,
			responsableId,
			parentId,
			objectClass,
			previousValue,
			objectId,
			parentClass);
	}

	public ChangeLogRecord(FieldChangedWatcher o, Long idUser, String nome) {
		this(null,
			null,
			false,
			null,
			null,
			null,
			null,
			null,
			null,
			null);
	}
}