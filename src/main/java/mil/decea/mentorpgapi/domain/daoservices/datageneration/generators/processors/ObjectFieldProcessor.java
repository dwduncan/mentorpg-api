package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors;

import mil.decea.mentorpgapi.domain.EmbeddedExternalData;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;

import java.lang.reflect.Field;

public class ObjectFieldProcessor extends AbstractFieldProcessor {


    public ObjectFieldProcessor(Field field) {
        super("ObjectFieldProcessor", field,"getEntity()","getIdentifiedRecord()");
    }

    public ObjectFieldProcessor(Field field, String entityPrefix, String reoPrefix) {
        super("ObjectFieldProcessor", field, entityPrefix, reoPrefix);
    }

    @Override
    protected String getCollectionForRecordFieldCase(CollectionForRecordField annotation) {

        /*String toCollection =".toList()";
        String toimport = "java.util.List";

        if (annotation.collectionOfType() != List.class){
            toCollection = ".collect(Collectors.toSet())";
            toimport = "java.util.Set";
        }

        requiredImports.add(toimport);
        String reverseMap = "().stream().map(" + annotation.elementsOfType().getSimpleName() + "::new)" + toCollection + ");";*/
        return null;// entityPrefix + "." + setterName + "(" + recordPrefix + "." + fieldName + reverseMap;
    }

    @Override
    protected String getObjectForRecordFieldCase() {

        requiredImports.add("org.springframework.beans.factory.annotation.Autowired");
        if (EmbeddedExternalData.class.isAssignableFrom(field.getType())){
            requiredImports.add("mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord");
            injections.add("\r\n@Autowired\r\nEmbeddedExternalDataAdapter " + fieldName + ";\r\n");
        }else{
            injections.add("\r\n@Autowired\r\n" + typeSimpleName + "Adapter "+ fieldName + ";\r\n");
        }

        return fieldName + ".with(" + entityPrefix + "." + getterName + "(), " + recordPrefix
                + "." + fieldName + "()).updateEntity()";
    }

    @Override
    protected String getEmbeddedExternalDataFieldCase() {
        return getObjectForRecordFieldCase();
    }

    @Override
    protected String getEmbeddedFieldCase() {
        requiredImports.add(field.getType().getName());
        return getObjectForRecordFieldCase();
    }

    @Override
    protected String getTemporalFieldCase() {
        requiredImports.add("mil.decea.mentorpgapi.util.DateTimeAPIHandler");
        return entityPrefix + "." + setterName + "(DateTimeAPIHandler.converterStringDate(" +
                recordPrefix + "." + fieldName + "())";
    }

    @Override
    protected String getOrElseCase() {
        return entityPrefix + "." + setterName + "(" + recordPrefix + "." + fieldName + "())";
    }

}
