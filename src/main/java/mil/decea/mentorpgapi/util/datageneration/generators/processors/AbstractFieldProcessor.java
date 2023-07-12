package mil.decea.mentorpgapi.util.datageneration.generators.processors;

import jakarta.persistence.Embedded;
import jakarta.validation.Constraint;
import lombok.Getter;
import mil.decea.mentorpgapi.domain.EmbeddedExternalData;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.util.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.util.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.util.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.temporal.Temporal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public abstract class AbstractFieldProcessor implements FieldProcessor {

    Field field;
    String fieldName;
    String setterName;
    String getterName;
    String entityPrefix;
    String recordPrefix;
    String typeSimpleName;
    String processorName;
    Set<String> requiredImports;

    final Set<String> constraints = new HashSet<>();

    final Set<String> injections = new HashSet<>();

    public AbstractFieldProcessor(String processorName, Field field) {
        this(processorName, field, "obj", "rec");
    }

    public AbstractFieldProcessor(String processorName, Field field, String entityPrefix, String recordPrefix) {
        this.processorName = processorName;
        this.entityPrefix = entityPrefix;
        this.recordPrefix = recordPrefix;
        this.field = field;
        this.fieldName = field.getName();
        this.typeSimpleName = field.getType().getSimpleName();
        String fCap = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        setterName = "set" +fCap;
        getterName =  boolean.class.isAssignableFrom(field.getType()) ? "is" + fCap : "get" +fCap;
        requiredImports = new HashSet<>();

        for(Annotation ann : field.getAnnotations()){
            if (ann.annotationType().getAnnotation(Constraint.class) != null){
                constraints.add(ann.toString().replace("jakarta.validation.constraints.",""));
                requiredImports.add(ann.annotationType().getName());
            }
        }
    }

    abstract String getCollectionForRecordFieldCase(CollectionForRecordField annotation);
    abstract String getObjectForRecordFieldCase();
    abstract String getEmbeddedExternalDataFieldCase();
    abstract String getEmbeddedFieldCase();
    abstract String getTemporalFieldCase();
    abstract String getOrElseCase();
    @Override
    public String processorName(){
        return processorName;
    }

    public Set<String> getRequiredImports(){
        return requiredImports;
    }

    public void checkFieldImport(Field field){
        if (!(ReflectionUtils.isLikePrimitiveOrString(field.getType()) ||
                Objects.equals(field.getDeclaringClass().getPackage(),field.getType().getPackage()))){
            requiredImports.add(field.getType().getName());
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractFieldProcessor that)) return false;
        return Objects.equals(processorName, that.processorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processorName);
    }


    @Override
    public String getStatement(){

        if (field != null && !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(NotForRecordField.class)) {
            CollectionForRecordField ofrf = field.getAnnotation(CollectionForRecordField.class);

            checkFieldImport(field);

            if (field.getType().isRecord()) {
                return null;
            } else if (ofrf != null && ofrf.elementsOfType() != Object.class) {
                return getCollectionForRecordFieldCase(ofrf);
            } else if (field.isAnnotationPresent(ObjectForRecordField.class)
                    || SequenceIdEntity.class.isAssignableFrom(field.getType())) {
                return getObjectForRecordFieldCase();
            } else if (EmbeddedExternalData.class.isAssignableFrom(field.getType())) {
                return getEmbeddedExternalDataFieldCase();
            } else if (field.isAnnotationPresent(Embedded.class)) {
                return getEmbeddedFieldCase();
            } else if (Temporal.class.isAssignableFrom(field.getType())) {
                return getTemporalFieldCase();
            } else {
                return getOrElseCase();
            }
        }
        return null;
    }

    String getConstrainsAnnotation(){

        StringBuilder a = new StringBuilder();
        for(String c : constraints){
            a.append(c).append("\n");
        }

        return a.toString();

    }

}
