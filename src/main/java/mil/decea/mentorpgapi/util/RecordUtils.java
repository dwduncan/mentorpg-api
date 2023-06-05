package mil.decea.mentorpgapi.util;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.NotForRecordField;
import mil.decea.mentorpgapi.domain.user.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class RecordUtils {

    Set<String> impConf = new HashSet<>();
    StringBuilder constructor;
    StringBuilder reverseConstructor;
    StringBuilder imps;
    StringBuilder main;
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

        String recName = classe.getSimpleName() + "Record";

        imps = new StringBuilder();
        String dir = "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";
        main = new StringBuilder("package " + classe.getPackage().getName());
        main.append(";\r\n{IMPORTS}\r\npublic record ");

        constructor = new StringBuilder("\r\n\tpublic ")
                .append(recName)
                .append("(")
                .append(classe.getSimpleName()).append(" obj) {\r\n\t\tthis(");

        reverseConstructor = new StringBuilder("\r\n\t@NotForRecordField\r\n\tpublic void set")
                .append(classe.getSimpleName())
                .append("(")
                .append(recName).append(" rec) {");

        main.append(recName).append("(\r\n");

        appendAttributes(classe,"obj.","rec.");

        main.append(") {");
        constructor.append(");\r\n\t}\r\n");
        reverseConstructor.append("\r\n\t}\r\n");
        main.append(constructor);
        main.append("}");
        /*String corpo = main.toString().replace("{IMPORTS}",imps.toString());
        FileWriter arq = new FileWriter(dir + recName + ".java");
        arq.write(corpo);
        arq.close();*/
        System.out.println(reverseConstructor);

        return classe.getPackage().getName() + "." + recName;
    }

    private void appendAttributes(Class<?> classe, String prefixObjReference, String prefixRecReference) throws IOException{

        for(Method method: classe.getMethods()){

            if (!method.isAnnotationPresent(NotForRecordField.class)) {
                try {
                    Field field = getMethodField(method);
                    if (field != null && !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(NotForRecordField.class)) {

                        if (method.getReturnType().isRecord()) {
                            new RecordUtils(method.getReturnType()).generateRecord();
                        } else if (field.isAnnotationPresent(Embedded.class)) {
                            String record = new RecordUtils(method.getReturnType()).generateRecord();
                            processAttribute(record, prefixObjReference,prefixRecReference);
                        } else {
                            processAttribute(method, prefixObjReference,prefixRecReference);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }
    private void processAttribute(Method method, String prefixObjReference, String prefixRecReference){

        String methodName = method.getName();
        Field field;
        String fieldName;
        try {
            field = getMethodField(method);
            fieldName = field.getName();
        } catch (NoSuchFieldException | NullPointerException e) {
            return;
        }

        NotNull nnAnnotation = field.getAnnotation(NotNull.class);
        String notNull;
        if (nnAnnotation != null) {
            notNull = nnAnnotation.message() != null ? "\t@NotNull(message=\"" + nnAnnotation.message() + "\")\r\n\t" : "\t@NotNull\r\n\t";
            String c = "jakarta.validation.constraints.NotNull";
            if (!impConf.contains(c)) {
                impConf.add(c);
                imps.append("import ").append(c).append(";\r\n");
            }
        }else{
            notNull = "\t";
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
            main.append(notNull).append("String ").append(fieldName);
        } else {

            if (!type.isPrimitive() && !(String.class.isAssignableFrom(type))
                    && !type.getPackage().equals(classe.getPackage()) && !impConf.contains(type.getName())) {
                imps.append("import ").append(type.getName()).append(";\r\n");
                impConf.add(type.getName());
            }

            reverseConstructor.append("\r\n\t\t").append(metodSetName).append(prefixRecReference).append(fieldName).append("());");
            constructor.append(prefixObjReference).append(methodName).append("()");
            if (Collection.class.isAssignableFrom(type)) {
                main.append(notNull).append(type.getSimpleName()).append("<?> ").append(fieldName);
            } else {
                main.append(notNull).append(type.getSimpleName()).append(" ").append(fieldName);
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

        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        main.append("\t").append(name).append(" ").append(fieldName);
        constructor.append("new ").append(name).append("(").append(prefixObjReference).append("get").append(name.replace("Record","")).append("())");
        reverseConstructor.append("\r\n\t\t").append(methodGetName).append(methodSetName).append(prefixRecReference).append(fieldName).append("());");
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
    public static void printReactModel(Class<?> classe){
        printReactModel(classe,true);
    }
    public static String printReactModel(Class<?> classe, boolean defaultExport){

        String reactInterfaceName = "I" + classe.getSimpleName();
        StringBuilder interfaceBuilder = new StringBuilder("export ").append(defaultExport ? "default " : "").append("interface ").append(reactInterfaceName).append(" {\r\n");
        StringBuilder fieldsDeclaretionBuilder = new StringBuilder("\r\n");
        StringBuilder defaultValuesBuilder = new StringBuilder("export const default").append(reactInterfaceName).append(" = {\r\n");

        StringBuilder classBuilder = new StringBuilder("export class ").append(classe.getSimpleName()).append(" implements ").append(reactInterfaceName).append(" {\r\n");
        StringBuilder classBuilderConstructor = new StringBuilder("\r\n\tpublic constructor(obj?:  ").append(reactInterfaceName).append(") {\r\n\r\n\t\tif (obj){\r\n");
        StringBuilder elseBuilderConstructor = new StringBuilder("else{\r\n");

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

            if (field.getType().isRecord()) {
                String recordName = printReactModel(field.getType(),false);
                fieldsDeclaretionBuilder.append("\t").append(campo).append(": ").append(recordName);
                defaultValuesBuilder.append("\t").append(campo).append(": default").append(recordName).append(";\r\n");

                classBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = obj.").append(campo).append(";");
                elseBuilderConstructor.append("\t\t\tthis.").append(campo).append(" = new ").append(field.getType().getSimpleName()).append("();");
            }else if (classe.isRecord() || (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())))) {

                final String tipoNome = field.getType().getSimpleName();
                NotNull nnAnnotation = field.getAnnotation(NotNull.class);
                boolean nullable = nnAnnotation == null;
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

        Map<String,String> delaredMethods = ReactMethodDeclaration.getDeclaredMethodsForClass(classe);
        classBuilderConstructor.append("\r\n\t\t}").append(elseBuilderConstructor).append("\r\n\t\t}\r\n\t}");
        interfaceBuilder.append(fieldsDeclaretionBuilder).append("\r\n");
        classBuilder.append(fieldsDeclaretionBuilder).append("\r\n").append(classBuilderConstructor);
        if (delaredMethods != null){
            for(String key : delaredMethods.keySet()){
                String value = delaredMethods.get(key);
                classBuilder.append("\t").append(value).append("\r\n");
                interfaceBuilder.append("\t").append(key);
            }
        }

        interfaceBuilder.append("\r\n}\r\n");
        classBuilder.append("\r\n}\r\n");
        System.out.println(interfaceBuilder);
        //System.out.println(defaultValuesBuilder.append("} as ").append(reactInterfaceName).append(";\r\n\r\n"));
        System.out.println(classBuilder);
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



        for(Method m : classe.getMethods()){
            if (m.getReturnType().isEnum()){

                StringBuilder fileBody = new StringBuilder();
                String enum_name = m.getReturnType().getSimpleName();

                Class<?> enumClass = m.getReturnType();

                fileBody.append("\r\nexport interface ").append(enum_name).append(" {").append("\r\n\tname: string,");
                Arrays.stream(enumClass.getDeclaredFields())
                        .filter(f -> f.getType().isPrimitive() || String.class.isAssignableFrom(f.getType()))
                        .forEach(f->{fileBody
                                .append("\r\n\t")
                                .append(f.getName())
                                .append(": ")
                                .append(getTypeScript(f.getType()))
                                .append(",");
                        });

                fileBody.append("\r\n}\r\n");

                fileBody.append("\r\nexport const enum").append(enum_name).append(": ").append(enum_name).append("[] = [\r\n");

                for(Field enumValue : m.getReturnType().getFields()){
                    fileBody.append("\t\t{ name: '").append(enumValue.getName()).append("'");

                    for(Field f : m.getReturnType().getDeclaredFields()){
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
                fileBody.append("];\r\n");

                if (!fileBody.toString().isBlank()) {
                    if (!targetDir.endsWith("/")) targetDir += "/";
                    FileWriter arq = new FileWriter(targetDir + enum_name + ".ts");
                    arq.write(fileBody.toString());
                    arq.close();
                }

                //System.out.println(fileBody);
            }
        }

    }



    public static void main(String... args) throws IOException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException {

        /*

        System.out.println(new File(targetDir).exists());

        StringBuilder fileBody = new StringBuilder();*/

        RecordUtils ru = new RecordUtils(User.class);
        ru.generateRecord();

        //RecordUtils.printReactModel(UserRecord.class);
        /*
        String targetDir = "/Users/duncandwdi.DECEA/IdeaProjects/PrototipoMentorPG3Next/src/model";
        exportEnumsToTypeScript(targetDir,User.class);
*/
    }

}
