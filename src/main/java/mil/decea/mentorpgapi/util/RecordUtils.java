package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import jakarta.validation.Constraint;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;
import mil.decea.mentorpgapi.domain.documents.UrlToDocumentRecord;
import mil.decea.mentorpgapi.domain.documents.UserDocument;
import mil.decea.mentorpgapi.domain.documents.UserDocumentRecord;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        main.append(";\r\n{IMPORTS}\r\npublic record ");
        String _constructorDeclaration = "\r\n\tpublic " + recName + "(" + classe.getSimpleName() +" obj) {\r\n\t\tthis(";

        constructor = new StringBuilder(_constructorDeclaration);

        String setterWay = "\r\n\t@NotForRecordField\r\n\tpublic void set";
        String constructorWay = "\r\n\t@NotForRecordField\r\n\tpublic ";

        String declaration = classe.getSimpleName() + "(" + recName + " rec) {";
        reverseConstructor = new StringBuilder(declaration);

        main.append(recName).append("(\r\n");

        appendAttributes(classe,prefixObjReference,"rec.");

        additionalConstructors = new StringBuilder();

        for(Constructor<?> cttr : classe.getDeclaredConstructors()){
            if (!cttr.isAnnotationPresent(NotForRecordField.class)) {
                if (cttr.getParameterCount() > 0) {
                    boolean a = false;
                    additionalConstructors.append("\r\n\tpublic ").append(recName).append("(");
                    List<SimpleParam> subCFields = new ArrayList<>(cttr.getParameterCount());
                    for (Parameter p : cttr.getParameters()) {
                        if (a) additionalConstructors.append(", ");
                        subCFields.add(new SimpleParam(p));
                        additionalConstructors.append(p.getType().getSimpleName()).append(" ").append(p.getName());
                        a = true;
                    }

                    a = false;
                    additionalConstructors.append(") {\r\n\t\tthis(");
                    for (SimpleParam cf : fullConstructorFields) {
                        if (a) additionalConstructors.append(",\r\n\t\t\t");
                        if (subCFields.contains(cf)) {
                            additionalConstructors.append(cf.name);
                        } else {
                            if (cf.type.isPrimitive()) {
                                if (boolean.class.isAssignableFrom(cf.type)) additionalConstructors.append("false");
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

        main.append(") {");
        constructor.append(");\r\n\t}\r\n");
        reverseConstructor.append("\r\n\t}\r\n");
        main.append(constructor).append(additionalConstructors);
        main.append("}");
        String corpo = main.toString().replace("{IMPORTS}",imps.toString());
        FileWriter arq = new FileWriter(dir + recName + ".java");
        arq.write(corpo);
        arq.close();
        System.out.println(setterWay + reverseConstructor);
        System.out.println(constructorWay + declaration + "\r\n\t\t set" + classe.getSimpleName() + "(rec);\r\n\t}");

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
                                String record = new RecordUtils(ofrf.elementsOfType()).generateRecord();
                                processAttribute(field, prefixObjReference, prefixRecReference, ofrf, false);
                            } else if (field.isAnnotationPresent(ObjectForRecordField.class)) {
                                String record = new RecordUtils(method.getReturnType()).generateRecord();
                                processAttribute(method, prefixObjReference, prefixRecReference);
                            } else if (field.isAnnotationPresent(Embedded.class)) {
                                String record = new RecordUtils(method.getReturnType()).generateRecord();
                                processAttribute(record, prefixObjReference, prefixRecReference);
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
        String fieldName = field.getName();

        StringBuilder annotations = new StringBuilder();
        for(Annotation ann : field.getAnnotations()){
            if (ann.annotationType().getAnnotation(Constraint.class) != null){
                annotations.append(ann.toString().replace("jakarta.validation.constraints.","")).append("\r\n");
                if (!impConf.contains(ann.annotationType().getName())) {
                    impConf.add(ann.annotationType().getName());
                    imps.append("import ").append(ann.annotationType().getName()).append(";\r\n");
                }
            }
        }

        Class<?> type = method.getReturnType();
        //String metodSetName = "this.s" + method.getName().substring(1) + "(";
        String metodSetName = "this." + (method.getName().startsWith("get") ? method.getName().replaceFirst("get","set") :
                method.getName().replaceFirst("is","set"))+ "(";


        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }


        if (LocalDate.class.isAssignableFrom(type) || LocalDateTime.class.isAssignableFrom(type)) {
            String c = "mil.decea.mentorpgapi.util.DateTimeAPIHandler";

            if (!impConf.contains(c)) {
                impConf.add(c);
                imps.append("import ").append(c).append(";\r\n");
            }
            reverseConstructor.append("\r\n\t\t").append(metodSetName).append("DateTimeAPIHandler.converterStringDate(")
                    .append(prefixRecReference).append(fieldName).append("()));");
            constructor.append("DateTimeAPIHandler.converter(").append(prefixObjReference).append(methodName).append("())+\"\"");
            main.append(annotations).append("String ").append(fieldName);
        } else {

            if (!type.isPrimitive() && !(String.class.isAssignableFrom(type))
                    && !type.getPackage().equals(classe.getPackage()) && !impConf.contains(type.getName())) {
                imps.append("import ").append(type.getName()).append(";\r\n");
                impConf.add(type.getName());
            }

            if (fieldName.equals("id")) reverseConstructor.append("\r\n\t\t").append(metodSetName).append(prefixRecReference).append(fieldName).append("());");
            else reverseConstructor.append("\r\n\t\t").append(metodSetName).append(prefixRecReference).append(fieldName).append("());");
            constructor.append(prefixObjReference).append(methodName).append("()");
            if (Collection.class.isAssignableFrom(type)) {
                main.append(annotations).append(type.getSimpleName()).append("<?> ").append(fieldName);
            } else {
                main.append(annotations).append(type.getSimpleName()).append(" ").append(fieldName);
            }

        }

        hasAddedField = true;
    }
    private void processAttribute(String recordField, String prefixObjReference, String prefixRecReference){

        String[] pack = recordField.split("\\.");
        String name = pack[pack.length-1];
        String objName =  name.replace("Record","(");
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
        String methodSetName = ".set" + objName;
        String methodGetName = "this.get" + objName + ")";

        if (!impConf.contains(recordField)) {
            impConf.add(recordField);
            imps.append("import ").append(recordField).append(";\r\n");
        }

        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        main.append("\t").append(name).append(" ").append(fieldName);
        constructor.append("new ").append(name).append("(").append(prefixObjReference).append("get").append(name.replace("Record","")).append("())");
        reverseConstructor.append("\r\n\t\t").append(methodGetName).append(methodSetName).append(prefixRecReference).append(fieldName).append("());");
        hasAddedField = true;
    }
    private void processAttribute(Method method, String prefixObjReference, String prefixRecReference){

        String name = method.getName().replaceFirst("get","");
        String typeName =  method.getReturnType().getSimpleName() + "Record";
        String packRec = method.getReturnType().getPackage().getName() + "." + typeName;
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
        String methodSetName = "this.set" + name + "(";

        if (!impConf.contains(packRec)) {
            impConf.add(packRec);
            imps.append("import ").append(packRec).append(";\r\n");
        }

        if (hasAddedField ) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        main.append("\t").append(typeName).append(" ").append(fieldName);
        constructor.append("new ").append(typeName).append("(").append(prefixObjReference).append(method.getName()).append("())");
        reverseConstructor.append("\r\n\t\t").append(methodSetName).append("new ").append(method.getReturnType().getSimpleName()).append("(").append(prefixRecReference).append(fieldName).append("()));");
        hasAddedField = true;
    }

    private void processAttribute(Field field, String prefixObjReference, String prefixRecReference, CollectionForRecordField annotation, boolean hasAppended){

        String fieldName = field.getName();
        String suffixMethod =  fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        String methodSetName = ".set" + suffixMethod + "(";
        String methodGetName = "get" + suffixMethod;
        String packRec = annotation.elementsOfType().getName() + "Record";
        String elemRec = annotation.elementsOfType().getSimpleName() + "Record";


        if (hasAddedField || hasAppended) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        String toCollection =".toList()";
        String collection = "List<"+elemRec+"> ";
        String toimport = "java.util.List";

        if (annotation.collectionOfType() != List.class){
            toCollection = ".collect(Collectors.toSet())";
            collection = "Set<"+elemRec+"> ";
            toimport = "java.util.Set";
        }

        if (!impConf.contains(packRec)) {
            impConf.add(packRec);
            imps.append("import ").append(packRec).append(";\r\n");
        }

        if (!impConf.contains(toimport)) {
            impConf.add(toimport);
            imps.append("import ").append(toimport).append(";\r\n");
        }

        String reverseMap = "().stream().map(" + annotation.elementsOfType().getSimpleName() + "::new)" + toCollection + ");";
        main.append("\t").append(collection).append(" ").append(fieldName);
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

        boolean b = false;

        if (BaseEntity.class.isAssignableFrom(classe)){
            b = true;
            fieldsDeclaretionBuilder.append("\r\n\tid:\tstring;");
            classBuilderConstructor.append("\t\t\tthis.id = obj.id;");
            elseBuilderConstructor.append("\t\t\tthis.id = '';");
        }

        for(Field field: classe.getDeclaredFields()){

            if (field.isAnnotationPresent(NotForRecordField.class)) continue;

            String campo = field.getName();

            if (b) {
                fieldsDeclaretionBuilder.append(";\r\n");
                classBuilderConstructor.append("\r\n");
                elseBuilderConstructor.append("\r\n");
            }

            if (Collection.class.isAssignableFrom(field.getType())){
                Class<?> elementClass = ElementsType.getElementType(field);
                String recordName = "I" + elementClass.getSimpleName();
                String type = recordName + "[]";
                fieldsDeclaretionBuilder.append("\t").append(campo).append(": ").append(type);
                classBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = obj.").append(campo).append(";");
                elseBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = [];");
                defaultValuesBuilder.append("\t").append(campo).append(": ").append(type).append(";\r\n");
                imports.append("import ").append(recordName).append(" from './").append(recordName).append("';\r\n");
            }else if (field.getType().isRecord()) {
                String recordName = exportReactModel(field.getType(), targetDir, false);
                fieldsDeclaretionBuilder.append("\t").append(campo).append(": ").append(recordName);
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
                    fieldsDeclaretionBuilder.append("\tid: string");
                    defaultValuesBuilder.append("\tid: '';\r\n");
                    classBuilderConstructor.append("\t\t\tthis.id").append(" = obj.id;");
                    elseBuilderConstructor.append("\t\t\tthis.id = '';");
                } else {
                    fieldsDeclaretionBuilder.append("\t").append(campo).append(": ").append(type);
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
        //RecordUtils ru = new RecordUtils(UserDocument.class);
        //ru.generateRecord();

        RecordUtils.exportReactModel(UrlToDocumentRecord.class,targetDirATD);

        //exportEnumsToTypeScript(targetDirATD, StatusDoc.class);

    }

}
