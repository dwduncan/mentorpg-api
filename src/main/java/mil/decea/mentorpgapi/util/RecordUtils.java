package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.user.*;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecordUtils {

    Set<String> impConf = new HashSet<>();
    StringBuilder constructor;
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

        imps = new StringBuilder("");
        String dir = "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";
        main = new StringBuilder("package " + classe.getPackage().getName());
        main.append(";\r\n{IMPORTS}\r\npublic record ");
        constructor = new StringBuilder("\r\n\tpublic ")
                .append(recName)
                .append("(")
                .append(classe.getSimpleName()).append(" obj) {\r\n\t\tthis(");

        main.append(recName).append("(\r\n");
/*

        if (BaseEntity.class.isAssignableFrom(classe)){
            hasAddedField = true;
            main.append("\tLong id");
            constructor.append("obj.getId()");
        }
*/

        appendAttributes(classe,"obj.");

        main.append(") {");
        constructor.append(");\r\n\t}\r\n");
        main.append(constructor);
        main.append("}");
        String corpo = main.toString().replace("{IMPORTS}",imps.toString());
        FileWriter arq = new FileWriter(dir + recName + ".java");
        arq.write(corpo);
        arq.close();

        return classe.getPackage().getName() + "." + recName;
    }

    private void appendAttributes(Class<?> classe, String prefixObjReference) throws IOException{

        /*for(Field field: classe.getDeclaredFields()){
            if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
                if (field.getType().isRecord()){
                    new RecordUtils(field.getType()).generateRecord();
                }else if (field.isAnnotationPresent(Embedded.class)) {
                    String record = new RecordUtils(field.getType()).generateRecord();
                    processAttribute(record,prefixObjReference);
                }else {
                    processAttribute(field,prefixObjReference);
                }
            }
        }*/


        for(Method method: classe.getMethods()){
            try {
                Field field = getMethodField(method);
                if (field != null && !Modifier.isStatic(field.getModifiers())) {

                    if (method.getReturnType().isRecord()){
                        new RecordUtils(method.getReturnType()).generateRecord();
                    }else if (field.isAnnotationPresent(Embedded.class)) {
                        String record = new RecordUtils(method.getReturnType()).generateRecord();
                        processAttribute(record,prefixObjReference);
                    }else {
                        processAttribute(method,prefixObjReference);
                    }
                }
            }catch (Exception ignored){}

        }
    }
    private void processAttribute(Field field, String prefixObjReference){

        String fieldName = field.getName();
        String prefix = field.getType().getSimpleName().equals("boolean") ? "is" : "get";
        String method = prefix + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        if (LocalDate.class.isAssignableFrom(field.getType()) || LocalDateTime.class.isAssignableFrom(field.getType())) {
            String c = "mil.decea.mentorpgapi.util.ConvertDateToMillis";
            if (!impConf.contains(c)) {
                impConf.add(c);
                imps.append("import ").append(c).append(";\r\n");
            }
            constructor.append("ConvertDateToMillis.converter(").append(prefixObjReference).append(method).append("())+\"\"");
            main.append("\tString ").append(field.getName());
        } else {

            if (!field.getType().isPrimitive() && !(String.class.isAssignableFrom(field.getType()))
                    && !field.getType().getPackage().equals(classe.getPackage()) && !impConf.contains(field.getType().getName())) {
                imps.append("import ").append(field.getType().getName()).append(";\r\n");
                impConf.add(field.getType().getName());
            }

            constructor.append(prefixObjReference).append(method).append("()");
            if (Collection.class.isAssignableFrom(field.getType())) {
                main.append("\t").append(field.getType().getSimpleName()).append("<?> ").append(field.getName());
            } else {
                main.append("\t").append(field.getType().getSimpleName()).append(" ").append(field.getName());
            }

        }
        hasAddedField = true;
    }
    private void processAttribute(Method method, String prefixObjReference){

        String methodName = method.getName();
        Field field;
        String fieldName;
        try {
            field = getMethodField(method);
            fieldName = field.getName();
        } catch (NoSuchFieldException | NullPointerException e) {
            return;
        }



        Class<?> type = method.getReturnType();

        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        if (LocalDate.class.isAssignableFrom(type) || LocalDateTime.class.isAssignableFrom(type)) {
            String c = "mil.decea.mentorpgapi.util.ConvertDateToMillis";
            if (!impConf.contains(c)) {
                impConf.add(c);
                imps.append("import ").append(c).append(";\r\n");
            }
            constructor.append("ConvertDateToMillis.converter(").append(prefixObjReference).append(methodName).append("())+\"\"");
            main.append("\tString ").append(fieldName);
        } else {

            if (!type.isPrimitive() && !(String.class.isAssignableFrom(type))
                    && !type.getPackage().equals(classe.getPackage()) && !impConf.contains(type.getName())) {
                imps.append("import ").append(type.getName()).append(";\r\n");
                impConf.add(type.getName());
            }

            constructor.append(prefixObjReference).append(methodName).append("()");
            if (Collection.class.isAssignableFrom(type)) {
                main.append("\t").append(type.getSimpleName()).append("<?> ").append(fieldName);
            } else {
                main.append("\t").append(type.getSimpleName()).append(" ").append(fieldName);
            }

        }
        hasAddedField = true;
    }
    private void processAttribute(String recordField, String prefixObjReference){

        String[] pack = recordField.split("\\.");
        String name = pack[pack.length-1];
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);

        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        main.append("\t").append(name).append(" ").append(fieldName);
        constructor.append("new ").append(name).append("(").append(prefixObjReference).append("get").append(name.replace("Record","")).append("())");
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
        StringBuilder defaultValuesBuilder = new StringBuilder("export const default").append(reactInterfaceName).append(" = {\r\n");

        boolean b = false;

        if (BaseEntity.class.isAssignableFrom(classe)){
            b = true;
            interfaceBuilder.append("\r\n\tid:\tstring,");
        }

        for(Field field: classe.getDeclaredFields()){
            String campo = field.getName();

            if (b) {
                interfaceBuilder.append(",\r\n");
            }

            if (field.getType().isRecord()) {
                String recordName = printReactModel(field.getType(),false);
                interfaceBuilder.append("\t").append(campo).append(": ").append(recordName);
                defaultValuesBuilder.append("\t").append(campo).append(": default").append(recordName).append(",\r\n");
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
                    interfaceBuilder.append("\tid: string");
                    defaultValuesBuilder.append("\tid: '',\r\n");
                } else {
                    interfaceBuilder.append("\t").append(campo).append(": ").append(type);
                    var val = switch (type){
                        case "string" -> "''";
                        case "number" -> "0";
                        case "[]" -> "[]";
                        case "boolean" -> "false";
                        default -> "{}";
                    };
                    defaultValuesBuilder.append("\t").append(campo).append(": ").append(val).append(",\r\n");
                }

                b = true;
            }
        }

        System.out.println(interfaceBuilder.append("\r\n}\r\n"));
        System.out.println(defaultValuesBuilder.append("} as ").append(reactInterfaceName).append(";\r\n\r\n"));
        return reactInterfaceName;
    }

    public static void main(String... args) throws IOException {
/*

        RecordUtils ru = new RecordUtils(User.class);
        ru.generateRecord();
*/

        RecordUtils.printReactModel(UserRecord.class);


        /*
            System.out.println("export const FORCASSINGULARES = [");
            for(ForcaSingular p : ForcaSingular.values()){
                System.out.println("\t'" + p.getSigla() + "',");
            }
            System.out.println("];\r\n");
        */
    }

}
