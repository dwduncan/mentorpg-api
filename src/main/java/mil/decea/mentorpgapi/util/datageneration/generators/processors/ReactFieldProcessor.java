package mil.decea.mentorpgapi.util.datageneration.generators.processors;

import lombok.Getter;
import mil.decea.mentorpgapi.util.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.util.datageneration.OptionalsRecordField;
import mil.decea.mentorpgapi.util.datageneration.generators.ReactFilesGenerator;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

@Getter
public class ReactFieldProcessor extends AbstractFieldProcessor {

    String targetDir;
    public ReactFieldProcessor(Field field, String targetDir) {
        super("ReactFieldProcessor", field);
        this.targetDir = targetDir;
    }

    String fieldsDeclaretionBuilder;
    String classBuilderConstructor;
    String elseBuilderConstructor;
    String defaultValuesBuilder;
    
    @Override
    protected String getCollectionForRecordFieldCase(CollectionForRecordField annotation) {

        return getOrElseCase();
    }

    @Override
    protected String getObjectForRecordFieldCase() {
        return getOrElseCase();
    }

    @Override
    protected String getEmbeddedExternalDataFieldCase() {
        return getOrElseCase();
    }

    @Override
    protected String getEmbeddedFieldCase() {
        return getOrElseCase();
    }

    @Override
    protected String getTemporalFieldCase() {
        String type = "string";
        fieldsDeclaretionBuilder = "\t" + fieldName + getOptional() + ": " + type;
        classBuilderConstructor = "\t\t\tthis." + fieldName + " = obj." + fieldName + ";";
        elseBuilderConstructor = "\t\t\tthis." + fieldName + " = '';";
        defaultValuesBuilder = "\t" + fieldName + ": '';\r\n";
        return fieldsDeclaretionBuilder;
    }

    @Override
    protected String getOrElseCase() {
        if (Collection.class.isAssignableFrom(field.getType())){
            Class<?> elementClass = ReflectionUtils.getElementType(field);
            String recordName = "I" + elementClass.getSimpleName();
            fieldsDeclaretionBuilder = "\t" + fieldName + getOptional() + ": " + recordName + "[]";
            classBuilderConstructor = "\t\t\tthis." + fieldName + " = obj." + fieldName + ";";
            elseBuilderConstructor = "\t\t\tthis." + fieldName+ " = [];";
            defaultValuesBuilder = "\t" + fieldName + ": " + recordName + "[]" + ";\r\n";
            requiredImports.add("import "+ recordName+" from './"+recordName+"';");
        }else if (field.getType().isRecord()) {
            String recordName = "I" + field.getType().getSimpleName();
            new ReactFilesGenerator(field.getType(), targetDir, false);
            fieldsDeclaretionBuilder = "\t" + fieldName + getOptional() + ": " + recordName;
            classBuilderConstructor = "\t\t\tthis." + fieldName + " = obj." + fieldName + ";";
            elseBuilderConstructor = "\t\t\tthis." + fieldName+ " = new " + field.getType().getSimpleName() + "();";
            defaultValuesBuilder = "\t" + fieldName + ": default" + recordName + ";\r\n";
            requiredImports.add("import {"+ recordName + ", " + field.getType().getSimpleName() + "} from './"+recordName+"';");
        }else if (field.getDeclaringClass().isRecord() 
                || (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())))) {
            String type = getJavascriptType();
            if (fieldName.equals("id")) {
                fieldsDeclaretionBuilder = "\tid" + getObjectForRecordFieldCase() + ": string";
                defaultValuesBuilder = "\tid: '';\r\n";
                classBuilderConstructor = "\t\t\tthis.id" + " = obj.id;";
                elseBuilderConstructor = "\t\t\tthis.id = '';";
            } else {
                var val = switch (type){
                    case "string","string | null" -> "''";
                    case "number" -> "0";
                    case "[]" -> "[]";
                    case "boolean" -> "false";
                    default -> "{}";
                };
                fieldsDeclaretionBuilder = "\t" + fieldName + getOptional() + ": " + type;
                classBuilderConstructor = "\t\t\tthis." + fieldName + " = obj." + fieldName + ";";
                elseBuilderConstructor = "\t\t\tthis." + fieldName + " = " + val + ";";
                defaultValuesBuilder = "\t" + fieldName + ": " + val + ";\r\n";
            }
        }
        
        return fieldsDeclaretionBuilder;
    }

    String getOptional(){
        boolean allFieldsAreOptional = field.getDeclaringClass().isAnnotationPresent(OptionalsRecordField.class);
        boolean thisFieldsIsOptional = field.isAnnotationPresent(OptionalsRecordField.class);
        return allFieldsAreOptional || thisFieldsIsOptional ? "?" : "";
    }
    
    private String getJavascriptType(){
        final String tipoNome = field.getType().getSimpleName();
        String type;
        if (Collection.class.isAssignableFrom(field.getType())){
            type = "[]";
        }else if (field.getType().isEnum()){
            type = "string";
        }else {
            type = switch (tipoNome) {
                case "String","LocalDate", "LocalTime", "LocalDateTime" -> "string";
                case "boolean", "Boolean" -> "boolean";
                case "Integer", "int", "float", "Float", "double", "Double", "long", "Long", "BigDecimal", "BigInteger" -> "number";
                default -> tipoNome;
            };
        }
        return type;
    }
    
}
