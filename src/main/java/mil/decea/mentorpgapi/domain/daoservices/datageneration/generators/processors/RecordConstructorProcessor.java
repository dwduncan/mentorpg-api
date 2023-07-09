package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors;

import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;

import java.lang.reflect.Field;
import java.util.List;

public class RecordConstructorProcessor extends AbstractFieldProcessor {


    public RecordConstructorProcessor(Field field) {
        super("RecordConstructorProcessor", field);
    }

    @Override
    protected String getCollectionForRecordFieldCase(CollectionForRecordField annotation) {

        String elemRec = annotation.elementsOfType().getSimpleName() + "Record";
        String toCollection =".toList()";
        String toimport = "java.util.List";

        if (annotation.collectionOfType() != List.class){
            toCollection = ".collect(Collectors.toSet())";
            toimport = "java.util.Set";
        }

        requiredImports.add(toimport);

        return entityPrefix + "." + getterName + "().stream().map(" + elemRec + "::new)" + toCollection;
    }

    @Override
    protected String getObjectForRecordFieldCase() {
        return getEmbeddedFieldCase();
    }

    @Override
    protected String getEmbeddedExternalDataFieldCase() {
        requiredImports.add("mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord");
        return "new EmbeddedExternalDataRecord(" + entityPrefix + "." + getterName + "())";
    }

    @Override
    protected String getEmbeddedFieldCase() {
        requiredImports.add(field.getType().getName());
        return "new " + field.getType().getSimpleName() + "Record (" + entityPrefix + "."
                + getterName + "())";
    }

    @Override
    protected String getTemporalFieldCase() {
        requiredImports.add("mil.decea.mentorpgapi.util.DateTimeAPIHandler");
        return "DateTimeAPIHandler.converter(" + entityPrefix + "." + getterName + "())";
    }

    @Override
    protected String getOrElseCase() {
        return entityPrefix + "." + getterName + "()";
    }

}
