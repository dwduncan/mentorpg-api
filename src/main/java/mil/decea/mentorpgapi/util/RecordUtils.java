package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.domain.user.UserRecord;

import java.io.FileWriter;
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

    public String generateRecord(){

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
        /*
        for(Field field: classe.getDeclaredFields()){
            if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
                if (hasAddedField) {
                    main.append(",\r\n");
                    constructor.append(",\r\n\t\t\t");
                }

                if (field.isAnnotationPresent(Embedded.class)){

                }


                String campo = field.getName();

                main.append("\t").append(field.getType().getSimpleName()).append(" ").append(field.getName());
                String metodo = campo.substring(0, 1).toUpperCase() + campo.substring(1);
                String pref = field.getType().getSimpleName().equals("boolean") ? "is" : "get";

                if (!field.getType().isPrimitive() && !(String.class.isAssignableFrom(field.getType()))
                        && !field.getType().getPackage().equals(classe.getPackage()) && !impConf.contains(field.getType().getName())) {
                    imps.append("import ").append(field.getType().getName()).append(";\r\n");
                    impConf.add(field.getType().getName());
                }
                constructor.append("obj.").append(pref).append(metodo).append("()");
                hasAddedField = true;
            }
        }
        */
        main.append(") {");
        constructor.append(");\r\n\t}\r\n");
        main.append(constructor);
        main.append("}");
        String corpo = main.toString().replace("{IMPORTS}",imps.toString());
        try {
            FileWriter arq = new FileWriter(dir + recName + ".java");
            arq.write(corpo);
            arq.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return corpo;
    }

    private void appendFields(Class<?> classe, String prefixObjReference){
        for(Field field: classe.getDeclaredFields()){
            if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {

                String campo = field.getName();
                String pref = field.getType().getSimpleName().equals("boolean") ? "is" : "get";
                String metodo = campo.substring(0, 1).toUpperCase() + campo.substring(1);

                if (field.isAnnotationPresent(Embedded.class)){
                    appendFields(field.getType(),prefixObjReference + pref + metodo + "().");
                }else{
                    if (hasAddedField) {
                        main.append(",\r\n");
                        constructor.append(",\r\n\t\t\t");
                    }


                    if (LocalDate.class.isAssignableFrom(field.getType()) || LocalDateTime.class.isAssignableFrom(field.getType())){
                        String c = "mil.decea.mentorpgapi.util.ConvertDateToMillis";
                        if (!impConf.contains(c)) {
                            impConf.add(c);
                            imps.append("import ").append(c).append(";\r\n");
                        }
                        constructor.append("ConvertDateToMillis.converter(").append(prefixObjReference).append(pref).append(metodo).append("())+\"\"");
                        main.append("\tString ").append(field.getName());
                    }else  {

                        if (!field.getType().isPrimitive() && !(String.class.isAssignableFrom(field.getType()))
                                && !field.getType().getPackage().equals(classe.getPackage()) && !impConf.contains(field.getType().getName())) {
                            imps.append("import ").append(field.getType().getName()).append(";\r\n");
                            impConf.add(field.getType().getName());
                        }

                        constructor.append(prefixObjReference).append(pref).append(metodo).append("()");
                        if (Collection.class.isAssignableFrom(field.getType())){
                            main.append("\t").append(field.getType().getSimpleName()).append("<?> ").append(field.getName());
                        }else{
                            main.append("\t").append(field.getType().getSimpleName()).append(" ").append(field.getName());
                        }

                    }

                    hasAddedField = true;
                }
            }
        }
    }

    public static void printReactModel(Class<?> classe){

        String reactInterfaceName = "I" + classe.getSimpleName();

        StringBuilder interfaceBuilder = new StringBuilder("export default interface ").append(reactInterfaceName).append(" {\r\n");
        StringBuilder defaultValuesBuilder = new StringBuilder("export const").append(reactInterfaceName).append(" = {\r\n");

        boolean b = false;

        if (BaseEntity.class.isAssignableFrom(classe)){
            b = true;
            interfaceBuilder.append("\r\n\tid:\tstring,");
        }

        for(Field field: classe.getDeclaredFields()){
            if (classe.isRecord() || (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())))) {
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
        System.out.println(defaultValuesBuilder.append("} as ").append(reactInterfaceName).append(";"));
    }



    public static void main(String... args){

        //RecordUtils ru = new RecordUtils(User.class);
        //ru.generateRecord();
        RecordUtils.printReactModel(UserRecord.class);
    }

}
