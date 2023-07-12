package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import jakarta.validation.Constraint;
import mil.decea.mentorpgapi.domain.EmbeddedExternalData;
import mil.decea.mentorpgapi.domain.EmbeddedExternalDataRecord;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.RecordFieldName;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.TrackOnlySelectedFields;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.util.datageneration.*;
import mil.decea.mentorpgapi.util.datageneration.generators.ReactExtrasToExport;
import mil.decea.mentorpgapi.util.datageneration.generators.RecordFileGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.temporal.Temporal;
import java.util.*;

public class RecordUtils {

    Set<String> impConf = new HashSet<>();
    StringBuilder constructor;
    StringBuilder additionalConstructors;
    StringBuilder reverseConstructor;
    StringBuilder imps;
    StringBuilder main;

    List<SimpleParam> fullConstructorFields;
    boolean hasAddedField = false;
    final Class<?> classe;
    public RecordUtils(Class<?> classe) {
        this.classe = classe;
    }

    static Set<String> createdRecords = new HashSet<>();

    /**
     *
     * @return o nome da classe record gerada
     */
    public String generateRecord() throws IOException {
        return generateRecord(false);
    }


    public String generateRecord(boolean hasAdded) throws IOException {
        hasAddedField = hasAdded;
        fullConstructorFields = new ArrayList<>();
        String recName = classe.getSimpleName() + "Record";
        String prefixObjReference = "obj.";
        imps = new StringBuilder();
        String dir = "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";
        main = new StringBuilder("package " + classe.getPackage().getName());
        main.append(";\r\n\r\n{IMPORTS}\r\npublic record ");
        String _constructorDeclaration = "\r\n\tpublic " + recName + "(" + classe.getSimpleName() +" obj) {\r\n\t\tthis(";

        constructor = new StringBuilder(_constructorDeclaration);

        String setterWay = "\r\n\t@NotForRecordField\r\n\tpublic ObjectChangesChecker ";

        String c = "mil.decea.mentorpgapi.domain.IdentifiedRecord";
        impConf.add(c);
        imps.append("import ").append(c).append(";\r\n");
        boolean isTracked = classe.isAnnotationPresent(TrackOnlySelectedFields.class);

        reverseConstructor = new StringBuilder().append("onValuesUpdated(IdentifiedRecord incomingData) {\r\n\r\n");
        reverseConstructor.append("\t\t").append(recName).append(" rec = (").append(recName).append(") incomingData;");

        String _changes = isTracked ? "ObjectChangesChecker changes = new ObjectChangesChecker<>(this, rec).getChangesList();\r\n\r\n" :
                "ObjectChangesChecker changes = new ArrayList();\r\n\r\n";
        reverseConstructor.append(_changes);

        main.append(recName).append("(\r\n");

        appendAttributes(classe,prefixObjReference,"rec.");

        additionalConstructors = new StringBuilder();

        for(Constructor<?> cttr : classe.getDeclaredConstructors()){
            if (!cttr.isAnnotationPresent(NotForRecordField.class)) {
                if (cttr.getParameterCount() > 0) {
                    boolean a = false;
                    additionalConstructors.append("\r\n\tpublic ").append(recName).append("(");
                    List<SimpleParam> subConstructorFields = new ArrayList<>(cttr.getParameterCount());
                    for (Parameter p : cttr.getParameters()) {
                        if (a) additionalConstructors.append(", ");
                        subConstructorFields.add(new SimpleParam(p));
                        String typeName = Temporal.class.isAssignableFrom(p.getType()) ? "String" : p.getType().getSimpleName();
                        additionalConstructors.append(typeName).append(" ").append(p.getName());
                        a = true;
                    }

                    a = false;
                    additionalConstructors.append(") {\r\n\t\tthis(");
                    for (SimpleParam constructorField : fullConstructorFields) {
                        if (a) additionalConstructors.append(",\r\n\t\t\t");
                        if (subConstructorFields.contains(constructorField)) {
                            additionalConstructors.append(constructorField.name);
                        } else {
                            if (constructorField.type.isPrimitive()) {
                                if (boolean.class.isAssignableFrom(constructorField.type)) additionalConstructors.append("false");
                                else additionalConstructors.append("0");
                            } else {
                                additionalConstructors.append("null");
                            }
                        }
                        a = true;
                    }
                    additionalConstructors.append(");\r\n\t}\r\n");
                }
            }
        }


        main.append(") implements IdentifiedRecord {");
        constructor.append(");\r\n\t}\r\n");
        reverseConstructor.append("\r\n\r\n\t\treturn changes;").append("\r\n\t}\r\n");
        main.append(constructor).append(additionalConstructors);
        main.append("}");
        String corpo = main.toString().replace("{IMPORTS}",imps.toString());
        FileWriter arq = new FileWriter(dir + recName + ".java");
        arq.write(corpo);
        arq.close();
        System.out.println(setterWay + reverseConstructor);

        return classe.getPackage().getName() + "." + recName;
    }

    private void appendAttributes(Class<?> classe, String prefixObjReference, String prefixRecReference) throws IOException{

        for(Method method: classe.getMethods()){

            if (!method.isAnnotationPresent(NotForRecordField.class)) {
                MethodDefaultValue methodDefaultValue = method.getAnnotation(MethodDefaultValue.class);
                try {
                    if (methodDefaultValue != null) {
                        processAttribute(methodDefaultValue, method.getReturnType());
                    }else{

                        Field field = getMethodField(method);

                        if (field != null && !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(NotForRecordField.class)) {
                            CollectionForRecordField ofrf = field.getAnnotation(CollectionForRecordField.class);
                            fullConstructorFields.add(new SimpleParam(field));
                            if (method.getReturnType().isRecord()) {
                                new RecordUtils(method.getReturnType()).generateRecord();
                            } else if (ofrf != null && ofrf.elementsOfType() != Object.class) {
                                checkOrCreateRecordFor(ofrf.elementsOfType());
                                processCollectionForRecordField(field, prefixObjReference, prefixRecReference, ofrf);
                            } else if (field.isAnnotationPresent(ObjectForRecordField.class)
                                    || SequenceIdEntity.class.isAssignableFrom(field.getType())) {
                                checkOrCreateRecordFor(method.getReturnType());
                                processObjectForRecordField(method, prefixObjReference, prefixRecReference);
                            } else if (EmbeddedExternalData.class.isAssignableFrom(field.getType())) {
                                processEmbeddedExternalDataField(field);
                            } else if (field.isAnnotationPresent(Embedded.class)) {
                                checkOrCreateRecordFor(method.getReturnType());
                                String record = method.getReturnType().getName() + "Record";
                                processEmbeddedField(record, prefixObjReference, prefixRecReference);
                            } else {
                                processAttribute(method, field, prefixObjReference, prefixRecReference);
                            }
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void checkOrCreateRecordFor(Class<?> _classe) throws IOException {
        String arq = "./src/main/java/" + _classe.getName().replaceAll("\\.","/") + "Record.java";
        File _file = new File(arq);
        if (!_file.exists() && createdRecords.add(arq)) new RecordUtils(_classe).generateRecord();
    }

    private void processAttribute(MethodDefaultValue methodDefaultValue, Class<?> returnType){
        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        if (!returnType.isPrimitive() && !(String.class.isAssignableFrom(returnType))
                && !returnType.getPackage().equals(classe.getPackage()) && !impConf.contains(returnType.getName())) {
            imps.append("import ").append(returnType.getName()).append(";\r\n");
            impConf.add(returnType.getName());
        }

        constructor.append(methodDefaultValue.defaultValue());
        main.append(returnType.getSimpleName()).append(" ").append(methodDefaultValue.fieldName());
        hasAddedField = true;
    }
    private void processAttribute(Method method, Field field, String prefixObjReference, String prefixRecReference){

        String methodName = method.getName();
        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();

        StringBuilder annotations = new StringBuilder();
        for(Annotation ann : field.getAnnotations()){
            if (ann.annotationType().getAnnotation(Constraint.class) != null){
                annotations.append(ann.toString().replace("jakarta.validation.constraints.","")).append("\r\n");
                processImports(ann.annotationType().getName());
            }
        }

        Class<?> type = method.getReturnType();
        //String metodSetName = "this.s" + method.getName().substring(1) + "(";
        String metodSetName = "this." + (method.getName().startsWith("get") ? method.getName().replaceFirst("get","set") :
                method.getName().replaceFirst("is","set"))+ "(";


        checkBrakeLineAndIdentation();


        if (!processIfTemporalField(field, prefixObjReference, prefixRecReference, annotations.toString())) {

            String typeComplementation = " ";/*
            if (!ReflectionUtils.isLikePrimitiveOrString(field.getType())
                    && !type.getPackage().equals(classe.getPackage()) && !impConf.contains(type.getName())) {
                typeComplementation = "Record ";
                processImports(type.getName() + "Record");
            }*/

            if (fieldName.equals("id")) reverseConstructor.append("\r\n\t\t").append(metodSetName).append(prefixRecReference).append(fieldName).append("());");
            else reverseConstructor.append("\r\n\t\t").append(metodSetName).append(prefixRecReference).append(fieldName).append("());");

            constructor.append(prefixObjReference).append(methodName).append("()");
            if (Collection.class.isAssignableFrom(type)) {
                processImports(type.getName());
                main.append(annotations).append(type.getSimpleName()).append("<?> ").append(fieldName);
            } else {
                main.append(annotations).append(type.getSimpleName()).append(typeComplementation).append(fieldName);
            }

        }

        hasAddedField = true;
    }


    public boolean processIfTemporalField(Field field, String prefixObjReference, String prefixRecReference, String annotations){
        boolean b = Temporal.class.isAssignableFrom(field.getType());
        if (b){
            String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();
            String setMethod = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            String getMethod = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            processImports("mil.decea.mentorpgapi.util.DateTimeAPIHandler");
            reverseConstructor.append("\r\n\t\t").append(setMethod).append("DateTimeAPIHandler.converterStringDate(")
                    .append(prefixRecReference).append(field.getName()).append("()));");
            constructor.append("DateTimeAPIHandler.converter(").append(prefixObjReference).append(getMethod).append("())+\"\"");
            main.append(annotations).append("String ").append(fieldName);
        }
        return b;
    }
    private void processEmbeddedField(String recordField, String prefixObjReference, String prefixRecReference){

        String[] pack = recordField.split("\\.");
        String name = pack[pack.length-1];
        String objName =  name.replace("Record","(");
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
        String methodSetName = ".onValuesUpdated";//"".set" + objName;
        String methodGetName = "this.get" + objName + ")";

        processImports(recordField);
        checkBrakeLineAndIdentation();

        main.append(name).append(" ").append(fieldName);
        constructor.append("new ").append(name).append("(").append(prefixObjReference).append("get").append(name.replace("Record","")).append("())");
        reverseConstructor.append("\r\n\t\t").append(methodGetName).append(methodSetName).append(prefixRecReference).append(fieldName).append("());");
        hasAddedField = true;
    }


    private void processEmbeddedExternalDataField(Field  field){
        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();
        String objName =  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String methodSetName = ".onValuesUpdated";
        String methodGetName = "this.get" + objName + ")";
        processImports(EmbeddedExternalDataRecord.class.getName());
        checkBrakeLineAndIdentation();

        main.append("EmbeddedExternalDataRecord ").append(fieldName).append("Record");
        constructor.append("new EmbeddedExternalDataRecord").append("(obj.get").append(objName).append("())");
        reverseConstructor.append("\r\n\t\t").append(methodGetName).append(methodSetName).append("rec.").append(fieldName).append("Record());");
        hasAddedField = true;
    }

    private void processImports(String... imports){
        for(String i : imports) {

            if (!impConf.contains(i) && !i.startsWith(classe.getPackage().getName())) {
                impConf.add(i);
                imps.append("\r\nimport ").append(i).append(";");
            }
        }
    }

    private void checkBrakeLineAndIdentation(){
        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }
    }
    private void processObjectForRecordField(Method method, String prefixObjReference, String prefixRecReference){

        String name = method.getName().replaceFirst("get","");
        String typeName =  method.getReturnType().getSimpleName() + "Record";
        String packRec = method.getReturnType().getPackage().getName() + "." + typeName;
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
        String methodSetName = "this.set" + name + "(";

        processImports(packRec);

        checkBrakeLineAndIdentation();

        main.append(typeName).append(" ").append(fieldName);
        constructor.append("new ").append(typeName).append("(").append(prefixObjReference).append(method.getName()).append("())");
        reverseConstructor.append("\r\n\t\t").append(methodSetName).append("new ").append(method.getReturnType().getSimpleName()).append("(").append(prefixRecReference).append(fieldName).append("()));");
        hasAddedField = true;
    }


    private void processCollectionForRecordField(Field field, String prefixObjReference, String prefixRecReference, CollectionForRecordField annotation){

        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();
        String suffixMethod =  fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        String methodSetName = ".set" + suffixMethod + "(";
        String methodGetName = "get" + suffixMethod;
        String packRec = annotation.elementsOfType().getName() + "Record";
        String elemRec = annotation.elementsOfType().getSimpleName() + "Record";

        checkBrakeLineAndIdentation();

        String toCollection =".toList()";
        String collection = "List<"+elemRec+"> ";
        String toimport = "java.util.List";

        if (annotation.collectionOfType() != List.class){
            toCollection = ".collect(Collectors.toSet())";
            collection = "Set<"+elemRec+"> ";
            toimport = "java.util.Set";
        }

        processImports(packRec, toimport);

        String reverseMap = "().stream().map(" + annotation.elementsOfType().getSimpleName() + "::new)" + toCollection + ");";
        main.append(collection).append(" ").append(fieldName);
        constructor.append(prefixObjReference).append(methodGetName).append("().stream().map(").append(elemRec).append("::new)").append(toCollection);
        reverseConstructor.append("\r\n\t\tthis").append(methodSetName).append(prefixRecReference).append(fieldName).append(reverseMap);
        hasAddedField = true;
    }

    private String getMethodFieldName(Method method){

        String fieldName;
        String methodName = method.getName();

        if (methodName.startsWith("get")){
            fieldName = methodName.length() > 4 ? methodName.substring(3,4).toLowerCase() + methodName.substring(4) : methodName.replace("get","").toLowerCase();
        }else if (methodName.startsWith("is")){
            fieldName = methodName.length() > 3 ? methodName.substring(2,3).toLowerCase() + methodName.substring(3) : methodName.replace("is","").toLowerCase();
        }else{
            fieldName = null;
        }

        return fieldName;
    }
    private Field getMethodField(Method method) throws NoSuchFieldException {

        String fieldName = getMethodFieldName(method);

        if (fieldName == null) return null;

        Class<?> actual = method.getDeclaringClass();

        while (actual != Object.class){
            try {
                return actual.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                actual = actual.getSuperclass();
            }
        }
        throw new NoSuchFieldException();
    }
    public static void exportReactModel(Class<?> classe, String targetDir) throws IOException{
        exportReactModel(classe, targetDir,true);
    }
    public static String exportReactModel(Class<?> classe, String targetDir, boolean defaultExport) throws IOException {

        String reactInterfaceName = "I" + classe.getSimpleName();
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


        for(Field field: classe.getDeclaredFields()){

            if (field.isAnnotationPresent(NotForRecordField.class)) continue;

            boolean thisFieldsIsOptional = field.isAnnotationPresent(OptionalsRecordField.class);

            boolean optional = allFieldsAreOptional || thisFieldsIsOptional;

            System.out.println(field.getName() + " " + optional + " " + (field.getAnnotations() != null ? field.getAnnotations().length : "0" ));

            String campo = field.getName();

            if (b) {
                fieldsDeclaretionBuilder.append(";\r\n");
                classBuilderConstructor.append("\r\n");
                elseBuilderConstructor.append("\r\n");
            }

            if (Collection.class.isAssignableFrom(field.getType())){
                Class<?> elementClass = ReflectionUtils.getElementType(field);
                String recordName = "I" + elementClass.getSimpleName();
                String type = recordName + "[]";
                fieldsDeclaretionBuilder.append("\t").append(campo).append(optional ? "?" : "").append(": ").append(type);
                classBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = obj.").append(campo).append(";");
                elseBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = [];");
                defaultValuesBuilder.append("\t").append(campo).append(": ").append(type).append(";\r\n");
                imports.append("import ").append(recordName).append(" from './").append(recordName).append("';\r\n");
            }else if (field.getType().isRecord()) {
                String recordName = exportReactModel(field.getType(), targetDir, false);
                fieldsDeclaretionBuilder.append("\t").append(campo).append(optional ? "?" : "").append(": ").append(recordName);
                defaultValuesBuilder.append("\t").append(campo).append(": default").append(recordName).append(";\r\n");
                imports.append("import {").append(recordName).append(", ").append(recordName.substring(1)).append("} from './").append(recordName).append("';\r\n");
                classBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = obj.").append(campo).append(";");
                elseBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = new ").append(field.getType().getSimpleName()).append("();");
                b = true;
            }else if (classe.isRecord() || (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())))) {

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

                if (campo.equals("id")) {
                    fieldsDeclaretionBuilder.append("\tid").append(optional ? "?" : "").append(": string");
                    defaultValuesBuilder.append("\tid: '';\r\n");
                    classBuilderConstructor.append("\t\t\tthis.id").append(" = obj.id;");
                    elseBuilderConstructor.append("\t\t\tthis.id = '';");
                } else {
                    fieldsDeclaretionBuilder.append("\t").append(campo).append(optional ? "?" : "").append(": ").append(type);
                    var val = switch (type){
                        case "string","string | null" -> "''";
                        case "number" -> "0";
                        case "[]" -> "[]";
                        case "boolean" -> "false";
                        default -> "{}";
                    };
                    classBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = obj.").append(campo).append(";");
                    elseBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = ").append(val).append(";");
                    defaultValuesBuilder.append("\t").append(campo).append(": ").append(val).append(";\r\n");
                }

                b = true;
            }
        }

        String functionsToExport = ReactExtrasToExport.getFunctions(classe);
        classBuilderConstructor.append("\r\n\t\t}").append(elseBuilderConstructor).append("\r\n\t\t}\r\n\t}");
        interfaceBuilder.append(fieldsDeclaretionBuilder).append("\r\n");
        classBuilder.append(fieldsDeclaretionBuilder).append("\r\n").append(classBuilderConstructor);
        imports.append("\r\n");
        interfaceBuilder.append("\r\n}\r\n");
        classBuilder.append("\r\n}\r\n");
        if (functionsToExport != null && !functionsToExport.isBlank()) classBuilder.append(functionsToExport);
        System.out.println(interfaceBuilder);
        System.out.println(classBuilder);

        if (!interfaceBuilder.toString().isBlank()) {
            if (!targetDir.endsWith("/")) targetDir += "/";
            FileWriter arq = new FileWriter(targetDir + reactInterfaceName + ".ts");
            arq.write(imports.toString());
            arq.write(interfaceBuilder.toString());
            arq.write(classBuilder.toString());
            arq.close();
        }

        return reactInterfaceName;
    }


    static String getTypeScript(Class<?> classe){
        if (String.class.isAssignableFrom(classe)) return "string";
        if (classe.isPrimitive()){
            if (boolean.class.isAssignableFrom(classe)) return "boolean";
            return "number";
        }
        return null;
    }
    public static void exportEnumsToTypeScript(String targetDir, Class<?> classe) throws IOException {
        if (classe.isEnum()){
            exportEnum(targetDir, classe);
        }else {
            for (Method m : classe.getMethods()) {
                if (m.getReturnType().isEnum()) {
                    exportEnum(targetDir, m.getReturnType());
                }
            }
        }
    }

    private static void exportEnum(String targetDir, Class<?> enumClass) throws IOException{

        StringBuilder fileBody = new StringBuilder();
        StringBuilder enumTypedScript = new StringBuilder();
        String enum_name = enumClass.getSimpleName();


        fileBody.append("\r\nexport interface ").append(enum_name).append(" {").append("\r\n\tname: string,");
        Arrays.stream(enumClass.getDeclaredFields())
                .filter(f -> f.getType().isPrimitive() || String.class.isAssignableFrom(f.getType()))
                .forEach(f->{
                    fileBody.append("\r\n\t").append(f.getName()).append(": ").append(getTypeScript(f.getType())).append(",");
                });

        fileBody.append("\r\n}\r\n");

        fileBody.append("\r\nexport const array").append(enum_name).append(": ").append(enum_name).append("[] = [\r\n");

        enumTypedScript.append("\r\nexport enum ").append(enum_name.toUpperCase()).append(" {");

        for(Field enumValue : enumClass.getFields()){
            fileBody.append("\t\t{ name: '").append(enumValue.getName()).append("'");
            enumTypedScript.append("\r\n\t").append(enumValue.getName()).append(" = ").append("'").append(enumValue.getName()).append("',");
            for(Field f : enumClass.getDeclaredFields()){

                if (f.getType().isPrimitive() || String.class.isAssignableFrom(f.getType())){
                    try{
                        Enum _enum = Enum.valueOf((Class<Enum>)enumValue.getType(),enumValue.getName());
                        f.setAccessible(true);
                        Object value = f.get(_enum);
                        String quotes = String.class.isAssignableFrom(f.getType()) ? "'" : "";
                        fileBody.append(", ").append(f.getName()).append(": ").append(quotes).append(value).append(quotes);
                    }catch (Exception ex){
                        throw new RuntimeException(ex);
                    }
                }
            }
            fileBody.append(" },\r\n");
        }
        enumTypedScript.append("\r\n}");
        fileBody.append("];\r\n").append(enumTypedScript);

        if (!fileBody.toString().isBlank()) {
            if (!targetDir.endsWith("/")) targetDir += "/";
            FileWriter arq = new FileWriter(targetDir + enum_name + ".ts");
            arq.write(fileBody.toString());
            arq.close();
        }

    }
    static class SimpleParam{
        public final String name;
        public final Class<?> type;

        public SimpleParam(Field f){
            name = f.getName();
            type = f.getType();
        }
        public SimpleParam(String name, Class<?> type) {
            this.name = name;
            this.type = type;
        }

        public SimpleParam(Parameter p) {
            name = p.getName();
            type = p.getType();
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SimpleParam that = (SimpleParam) o;
            return Objects.equals(name, that.name) && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, type);
        }
    }

    public static void main(String... args) throws IOException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {

        String targetDirATD = "/Users/duncandwdi.DECEA/IdeaProjects/PrototipoMentorPG3Next/src/api/model";

        String targetDirHome = "/OneDrive/001_ProjetosPessoais/004_Cursos/PrototipoMentorPG3Next/src/api/model";

        /*

        System.out.println(new File(targetDir).exists());
        RecordUtils ru = new RecordUtils(User.class);
        ru.generateRecord();

        RecordUtils.exportReactModel(AuthUserRecord.class,targetDirHome);

        RecordUtils.exportReactModel(UserRecord.class,targetDirATD);

        exportEnumsToTypeScript(targetDirATD, User.class);

        */

        new RecordFileGenerator(User.class).createFile();

        //new AdapterBuilder(LinhaDePesquisa.class).build(true);

        //RecordUtils.exportReactModel(UserRecord.class,targetDirHome);

        //exportEnumsToTypeScript(targetDirHome, Posto.class);

    }

}
