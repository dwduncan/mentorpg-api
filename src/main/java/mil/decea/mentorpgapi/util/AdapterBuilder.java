package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import jakarta.validation.Constraint;
import mil.decea.mentorpgapi.domain.EmbeddedExternalData;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdapterBuilder {

    Set<String> impConf = new HashSet<>();
    StringBuilder imps;
    final Class<?> classe;
    
    String recordClass;
    StringBuilder fileBody = new StringBuilder();
    StringBuilder injections = new StringBuilder();
    boolean hasAddedField;
    String targetDir;

    boolean embeddableExternalData;
    public AdapterBuilder(Class<?> classe) {
        this.classe = classe;
        this.targetDir =  "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";;
        imps = new StringBuilder();
        recordClass = classe.getSimpleName() + "Record";
        embeddableExternalData = EmbeddedExternalData.class.isAssignableFrom(classe);
    }

    public String build() throws IOException {

        String className = classe.getSimpleName() + "Adapter";

        StringBuilder classDeclaration = new StringBuilder("\r\n\r\n@NoArgsConstructor\r\n").append("@Service\r\n").append("public class ")
                .append(className).append(" extends AbstractEntityDTOAdapter<")
                .append(classe.getSimpleName()).append(", ").append(recordClass).append("> {\r\n");

        impConf.add("mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter");
        impConf.add("import lombok.NoArgsConstructor");
        impConf.add("import mil.decea.mentorpgapi.util.DateTimeAPIHandler");
        impConf.add("import org.springframework.stereotype.Service");

        imps.append("\r\nimport mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter;")
                .append("\r\nimport lombok.NoArgsConstructor;")
                .append("\r\nimport org.springframework.stereotype.Service;");


        fileBody.append("\r\n\r\n\t@Override\r\n\tpublic ").append(recordClass)
                .append(" generateRecord() { return new ").append(recordClass)
                .append("(getEntity());}");

        fileBody.append("\r\n\r\n\tpublic ").append(classe.getSimpleName()).append(" updateEntity() {");

        for(Method method: classe.getMethods()){
            if (!method.isAnnotationPresent(NotForRecordField.class)) {
                MethodDefaultValue methodDefaultValue = method.getAnnotation(MethodDefaultValue.class);
                try {
                    Field field = getMethodField(method);

                    if (field != null && !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(NotForRecordField.class)) {
                        CollectionForRecordField ofrf = field.getAnnotation(CollectionForRecordField.class);

                        if (method.getReturnType().isRecord()) {
                            continue;
                        } else if (ofrf != null && ofrf.elementsOfType() != Object.class) {
                            //processAttribute(field,ofrf);
                        } else if (field.isAnnotationPresent(ObjectForRecordField.class)) {
                            processAttribute(method);
                        } else if (EmbeddedExternalData.class.isAssignableFrom(field.getType())) {
                            processAttribute(field.getName());
                        }  else if (field.isAnnotationPresent(Embedded.class)) {
                            String adapter = new AdapterBuilder(method.getReturnType()).build();
                            processAttribute(field.getType(), adapter);
                        }  else {
                            processAttribute(method, field);
                        }
                    }

                } catch (Exception ignored) {
                }
            }
        }

        fileBody.append("\r\n\t\treturn getEntity();\r\n\t}\r\n}");

        if (!fileBody.toString().isBlank()) {
            if (!targetDir.endsWith("/")) targetDir += "/";
            FileWriter arq = new FileWriter(targetDir + className + ".java");
            arq.write("package " + classe.getPackage().getName() + ";\r\n\r\n");
            arq.write(imps.toString());
            arq.write(classDeclaration.toString());
            arq.write(injections.toString());
            arq.write(fileBody.toString());
            arq.close();
        }
        return className;
    }
    private void processAttribute(Field field, CollectionForRecordField annotation){

        String fieldName = field.getName();
        String suffixMethod =  fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        String methodSetName = ".set" + suffixMethod + "(";
        String packRec = annotation.elementsOfType().getName() + "Record";

        if (hasAddedField) {
            fileBody.append(",\r\n\t\t");
        }

        String toCollection =".toList()";
        String toimport = "java.util.List";

        if (annotation.collectionOfType() != List.class){
            toCollection = ".collect(Collectors.toSet())";
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
        fileBody.append("\r\n\t\tgetEntity()").append(methodSetName).append("getIdentifiedRecord().").append(fieldName).append(reverseMap);
        hasAddedField = true;
    }

    private void processAttribute(Method method){

        String name = method.getName().replaceFirst("get","");
        String typeName =  method.getReturnType().getSimpleName() + "Record";
        String packRec = method.getReturnType().getPackage().getName() + "." + typeName;
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
        String methodSetName = "getEntity().set" + name + "(";

        if (!impConf.contains(packRec)) {
            impConf.add(packRec);
            imps.append("import ").append(packRec).append(";\r\n");
        }

        fileBody.append("\r\n\t\t").append(methodSetName).append("new ").append(method.getReturnType().getSimpleName()).append("(getIdentifiedRecord().").append(fieldName).append("()));");
        hasAddedField = true;
    }

    private void processAttribute(String fieldName){

        String objName =  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String methodGetName = "getEntity().get" + objName + "()";
        injections.append("\r\n@Autowired\r\n").append("EmbeddedExternalDataAdapter ").append(fieldName).append(";\r\n");
        fileBody.append("\r\n\t\t").append(fieldName).append(".with(").append(methodGetName).append(", ").append("getIdentifiedRecord().").append(fieldName).append("Record()).updateEntity();");
        hasAddedField = true;
    }

    private void processAttribute(Class<?> fieldType, String adapter){

        String objName =  adapter.replace("Adapter","");
        String fieldName = adapter.substring(0, 1).toLowerCase() + adapter.substring(1);
        String methodGetName = "getEntity().get" + objName + "()";

        if (EmbeddedExternalData.class.isAssignableFrom(fieldType)){
            injections.append("\r\n@Autowired\r\n").append("EmbeddedExternalDataAdapter ").append(fieldName).append(";\r\n");
        }else{
            injections.append("\r\n@Autowired\r\n").append(adapter).append(" ").append(fieldName).append(";\r\n");
        }
        fileBody.append("\r\n\t\t").append(fieldName).append(".with(").append(methodGetName).append(", ").append("getIdentifiedRecord().").append(fieldName).append("Record()).updateEntity();");


        hasAddedField = true;
    }

    private void processAttribute(Method method, Field field){

        String fieldName = field.getName();

        Class<?> type = method.getReturnType();
        String metodSetName = "getEntity()." + (method.getName().startsWith("get") ? method.getName().replaceFirst("get","set") :
                method.getName().replaceFirst("is","set"))+ "(";

        if (LocalDate.class.isAssignableFrom(type) || LocalDateTime.class.isAssignableFrom(type)) {
            String c = "mil.decea.mentorpgapi.util.DateTimeAPIHandler";

            if (!impConf.contains(c)) {
                impConf.add(c);
                imps.append("import ").append(c).append(";\r\n");
            }
            fileBody.append("\r\n\t\t").append(metodSetName).append("DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().")
                    .append(fieldName).append("()));");
        } else {

            if (!type.isPrimitive() && !(String.class.isAssignableFrom(type))
                    && !type.getPackage().equals(classe.getPackage()) && !impConf.contains(type.getName())) {
                imps.append("import ").append(type.getName()).append(";\r\n");
                impConf.add(type.getName());
            }

            if (fieldName.equals("id")) fileBody.append("\r\n\t\t").append(metodSetName).append("getIdentifiedRecord().").append(fieldName).append("());");
            else fileBody.append("\r\n\t\t").append(metodSetName).append("getIdentifiedRecord().").append(fieldName).append("());");
        }

        hasAddedField = true;
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
}
