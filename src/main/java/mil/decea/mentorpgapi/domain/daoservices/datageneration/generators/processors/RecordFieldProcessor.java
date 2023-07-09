package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors;

import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

public class RecordFieldProcessor extends AbstractFieldProcessor {
    private boolean requestNewRecord;
    public RecordFieldProcessor(Field field) {
        super("RecordFieldProcessor", field);
    }

    @Override
    protected String getCollectionForRecordFieldCase(CollectionForRecordField annotation) {

        String elemRec = annotation.elementsOfType().getSimpleName() + "Record";
        String collection = "List<"+elemRec+"> ";
        String toimport = "java.util.List";
        requestNewRecord = true;

        if (annotation.collectionOfType() != List.class){
            collection = "Set<"+elemRec+"> ";
            toimport = "java.util.Set";
        }

        requiredImports.add(toimport);

        return collection + " " + fieldName;
    }

    @Override
    protected String getObjectForRecordFieldCase() {
        requestNewRecord = true;
        return typeSimpleName + "Record " + fieldName;
    }

    @Override
    protected String getEmbeddedExternalDataFieldCase() {
        return "EmbeddedExternalDataRecord " + fieldName;
    }

    @Override
    protected String getEmbeddedFieldCase() {
        requestNewRecord = true;
        return typeSimpleName + "Record " + fieldName;
    }

    @Override
    protected String getTemporalFieldCase() {
        return getConstrainsAnnotation() + "String " + fieldName;
    }

    @Override
    protected String getOrElseCase() {
        String generic = Collection.class.isAssignableFrom(field.getType()) ? "<?> " : " ";
        return getConstrainsAnnotation() + typeSimpleName + generic + fieldName;
    }

    public boolean isRequestNewRecord() {
        return requestNewRecord;
    }
}
