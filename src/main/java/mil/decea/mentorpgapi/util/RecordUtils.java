package mil.decea.mentorpgapi.util;

import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.user.Contato;
import mil.decea.mentorpgapi.domain.user.Endereco;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class RecordUtils<T> {



    public static String generateRecord(Class<?> classe){

        String recName = classe.getSimpleName() + "Record";
        StringBuilder imps = new StringBuilder("");
        String dir = "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";
        StringBuilder sb = new StringBuilder("package " + classe.getPackage().getName());
        sb.append(";\r\n{IMPORTS}\r\npublic record ");
        StringBuilder constructor = new StringBuilder("\r\n\tpublic ")
                .append(recName)
                .append("(")
                .append(classe.getSimpleName()).append(" obj) {\r\n\t\tthis(");

        sb.append(recName).append("(\r\n");
        boolean b = false;
        Set<String> impConf = new HashSet<>();

        if (BaseEntity.class.isAssignableFrom(classe)){
            b = true;
            sb.append("\tLong id");
            constructor.append("obj.getId()");
        }

        for(Field field: classe.getDeclaredFields()){
            if (!(Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers()))) {
                if (b) {
                    sb.append(",\r\n");
                    constructor.append(",\r\n\t\t\t");
                }
                String campo = field.getName();

                sb.append("\t").append(field.getType().getSimpleName()).append(" ").append(field.getName());
                String metodo = campo.substring(0, 1).toUpperCase() + campo.substring(1);
                String pref = field.getType().getSimpleName().equals("boolean") ? "is" : "get";

                if (!field.getType().isPrimitive() && !(String.class.isAssignableFrom(field.getType()))
                        && !field.getType().getPackage().equals(classe.getPackage()) && !impConf.contains(field.getType().getName())) {
                    imps.append("import ").append(field.getType().getName()).append(";\r\n");
                    impConf.add(field.getType().getName());
                }
                constructor.append("obj.").append(pref).append(metodo).append("()");
                b = true;
            }
        }
        sb.append(") {");
        constructor.append(");\r\n\t}\r\n");
        sb.append(constructor);
        sb.append("}");
        String corpo = sb.toString().replace("{IMPORTS}",imps.toString());
        try {
            FileWriter arq = new FileWriter(dir + recName + ".java");
            arq.write(corpo);
            arq.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return corpo;
    }



    public static void main(String... args){

        System.out.println(generateRecord(Contato.class));

    }

}
