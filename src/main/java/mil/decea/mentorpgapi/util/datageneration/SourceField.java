package mil.decea.mentorpgapi.util.datageneration;

import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SourceField {

    final Field field;

    String fieldName;
    String setterName;
    String getterName;
    Set<String> requiredImports = new HashSet<>();

    String recordFieldDeclaration;
    String recordConstructorDeclaration;
    String typeSimpleName;
    String adapterDeclaration;
    String entityPrefix = "obj";
    String recordPrefix = "rec";

    public SourceField(Field field) {
        this.field = field;
        this.fieldName = field.getName();
        this.typeSimpleName = field.getType().getSimpleName();
        String fCap = fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        setterName = "set" +fCap;
        getterName = "get" +fCap;
    }


    void generateFields(){

        if (!(ReflectionUtils.isLikePrimitiveOrString(field.getType()) ||
            Objects.equals(field.getDeclaringClass().getPackage(),field.getType().getPackage()))){
            requiredImports.add(field.getType().getName());
        }

        if (field.isAnnotationPresent(ObjectForRecordField.class)
                || SequenceIdEntity.class.isAssignableFrom(field.getType())) {
            recordFieldDeclaration = typeSimpleName + "Record " + field.getName() + ";";
            recordConstructorDeclaration = "new " + typeSimpleName + "(" + entityPrefix + "." + fieldName + "())";
            adapterDeclaration = setterName + "(new " + typeSimpleName + "(" + recordPrefix
                    + "." + fieldName + "()))";
        }
    }

}
