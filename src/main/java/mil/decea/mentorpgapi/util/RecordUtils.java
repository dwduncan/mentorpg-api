package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.user.*;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
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

        if (BaseEntity.class.isAssignableFrom(classe)){
            hasAddedField = true;
            main.append("\tLong id");
            constructor.append("obj.getId()");
        }

        appendFields(classe,"obj.");

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

    private void appendFields(Class<?> classe, String prefixObjReference) throws IOException{
        for(Field field: classe.getDeclaredFields()){
            if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
                if (field.getType().isRecord()){
                    new RecordUtils(field.getType()).generateRecord();
                }else if (field.isAnnotationPresent(Embedded.class)) {
                    String record = new RecordUtils(field.getType()).generateRecord();
                    processField(record,prefixObjReference);
                }else {
                    processField(field,prefixObjReference);
                }
            }
        }
    }

    private void processField(Field field, String prefixObjReference){

        String campo = field.getName();
        String pref = field.getType().getSimpleName().equals("boolean") ? "is" : "get";
        String metodo = campo.substring(0, 1).toUpperCase() + campo.substring(1);

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
            constructor.append("ConvertDateToMillis.converter(").append(prefixObjReference).append(pref).append(metodo).append("())+\"\"");
            main.append("\tString ").append(field.getName());
        } else {

            if (!field.getType().isPrimitive() && !(String.class.isAssignableFrom(field.getType()))
                    && !field.getType().getPackage().equals(classe.getPackage()) && !impConf.contains(field.getType().getName())) {
                imps.append("import ").append(field.getType().getName()).append(";\r\n");
                impConf.add(field.getType().getName());
            }

            constructor.append(prefixObjReference).append(pref).append(metodo).append("()");
            if (Collection.class.isAssignableFrom(field.getType())) {
                main.append("\t").append(field.getType().getSimpleName()).append("<?> ").append(field.getName());
            } else {
                main.append("\t").append(field.getType().getSimpleName()).append(" ").append(field.getName());
            }

        }
        hasAddedField = true;
    }

    private void processField(String recordField, String prefixObjReference){

        String[] pack = recordField.split("\\.");
        String name = pack[pack.length-1];
        String campo = name.substring(0, 1).toLowerCase() + name.substring(1);

        if (hasAddedField) {
            main.append(",\r\n");
            constructor.append(",\r\n\t\t\t");
        }

        main.append("\t").append(name).append(" ").append(campo);
        constructor.append("new ").append(name).append("(").append(prefixObjReference).append("get").append(name.replace("Record","")).append("())");
    }
    public static void printReactModel(Class<?> classe){
        printReactModel(classe,true);
    }
    public static void printReactModel(Class<?> classe, boolean defaultExport){

        String reactInterfaceName = "I" + classe.getSimpleName();
        StringBuilder interfaceBuilder = new StringBuilder("export ").append(defaultExport ? "default " : "").append("interface ").append(reactInterfaceName).append(" {\r\n");
        StringBuilder defaultValuesBuilder = new StringBuilder("export const default").append(reactInterfaceName).append(" = {\r\n");

        boolean b = false;

        if (BaseEntity.class.isAssignableFrom(classe)){
            b = true;
            interfaceBuilder.append("\r\n\tid:\tstring,");
        }

        for(Field field: classe.getDeclaredFields()){
            if (field.getType().isRecord()) {
                printReactModel(field.getType(),false);
            }else if (classe.isRecord() || (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())))) {

                if (b) {
                    interfaceBuilder.append(",\r\n");
                }

                final String tipoNome = field.getType().getSimpleName();
                String campo = field.getName();
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
    }



    public static void main(String... args) throws IOException{

        /*RecordUtils ru = new RecordUtils(User.class);
        ru.generateRecord();*/
        RecordUtils.printReactModel(UserRecord.class);


        /*System.out.println("export const FORCASSINGULARES = [");
        for(ForcaSingular p : ForcaSingular.values()){
            System.out.println("\t'" + p.getSigla() + "',");
        }
        System.out.println("];\r\n");*/
    }

}
