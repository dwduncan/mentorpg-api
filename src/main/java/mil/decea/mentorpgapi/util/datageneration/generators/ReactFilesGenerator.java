package mil.decea.mentorpgapi.util.datageneration.generators;

import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.util.datageneration.OptionalsRecordField;
import mil.decea.mentorpgapi.util.datageneration.generators.processors.ReactFieldProcessor;

import java.util.*;

public class ReactFilesGenerator extends AbstractFilesGenerator{
    Set<String> injections = new HashSet<>();
    Set<String> requiredImports = new HashSet<>();
    List<ReactFieldProcessor> fields;
    List<String> methods = new ArrayList<>(2);
    String reactInterfaceName;
    String targetDir;
    boolean defaultExport;
    public ReactFilesGenerator(Class<?> classe, String targetDir, boolean defaultExport) {
        super(classe);
        this.targetDir = targetDir;
        this.defaultExport = defaultExport;
        reactInterfaceName = "I" + classe.getSimpleName();
        init();
    }

    private void init(){

        fields = Arrays.stream(classe.getDeclaredFields()).map(f -> new ReactFieldProcessor(f, targetDir)).toList();


        StringBuilder fieldsDeclaretionBuilder = new StringBuilder("\r\n");
        StringBuilder defaultValuesBuilder = new StringBuilder("export const default").append(reactInterfaceName).append(" = {\r\n");
        StringBuilder imports = new StringBuilder();

        StringBuilder classBuilder = new StringBuilder("export class ").append(classe.getSimpleName()).append(" implements ").append(reactInterfaceName).append(" {\r\n");
        StringBuilder classBuilderConstructor = new StringBuilder("\r\n\tpublic constructor(obj?:  ").append(reactInterfaceName).append(") {\r\n\r\n\t\tif (!!obj){\r\n");
        StringBuilder elseBuilderConstructor = new StringBuilder("else{\r\n");
        StringBuilder interfaceBuilder = new StringBuilder();
        interfaceBuilder.append(ReactExtrasToExport.getImports(classe));
        interfaceBuilder.append("export ").append(defaultExport ? "default " : "").append("interface ").append(reactInterfaceName).append(" {\r\n");

        boolean allFieldsAreOptional = classe.isAnnotationPresent(OptionalsRecordField.class);

        boolean b = false;

        if (SequenceIdEntity.class.isAssignableFrom(classe)){
            b = true;
            fieldsDeclaretionBuilder.append("\r\n\tid:\tstring;");
            classBuilderConstructor.append("\t\t\tthis.id = obj.id;");
            elseBuilderConstructor.append("\t\t\tthis.id = '';");
        }

        for(ReactFieldProcessor field: fields){

            if (b) {
                fieldsDeclaretionBuilder.append(";\r\n");
                classBuilderConstructor.append("\r\n");
                elseBuilderConstructor.append("\r\n");
            }

            fieldsDeclaretionBuilder.append(field.getFieldsDeclaretionBuilder());
            classBuilderConstructor.append(field.getClassBuilderConstructor());
            elseBuilderConstructor.append(field.getElseBuilderConstructor());
            defaultValuesBuilder.append(field.getDefaultValuesBuilder());
            requiredImports.addAll(field.getRequiredImports());
        }

        String functionsToExport = ReactExtrasToExport.getFunctions(classe);
        classBuilderConstructor.append("\r\n\t\t}").append(elseBuilderConstructor).append("\r\n\t\t}\r\n\t}");
        interfaceBuilder.append(fieldsDeclaretionBuilder).append("\r\n");
        classBuilder.append(fieldsDeclaretionBuilder).append("\r\n").append(classBuilderConstructor);
        interfaceBuilder.append("\r\n}\r\n");
        classBuilder.append("\r\n}\r\n");
        if (functionsToExport != null && !functionsToExport.isBlank()) classBuilder.append(functionsToExport);

        getMethods().add(fieldsDeclaretionBuilder.toString());
        getMethods().add(classBuilderConstructor.toString());
        getMethods().add(elseBuilderConstructor.toString());
        getMethods().add(defaultValuesBuilder.toString());
    }

    @Override
    public String getClassNameStatement() {
        return "";
    }

    @Override
    public String getSimpleFileName() {
        return reactInterfaceName + ".ts";
    }

    @Override
    public List<String> getMethods() {
        return methods;
    }

    @Override
    public List<String> getConstructors() {
        return new ArrayList<>();
    }

    @Override
    public Set<String> getRequiredImports() {
        return requiredImports;
    }

    @Override
    public Set<String> getClassFields() {
        return injections;
    }


    @Override
    public String getTargetDir() {
        return targetDir;
    }

}
