package mil.decea.mentorpgapi.util;

import jakarta.persistence.Embedded;
import mil.decea.mentorpgapi.domain.EmbeddedExternalData;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.RecordFieldName;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;

import java.io.FileWriter;
import java.io.IOException;
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

    String adapterName;
    String adapterSimpleName;

    boolean embeddableExternalData;
    public AdapterBuilder(Class<?> classe) {
        this.classe = classe;
        this.targetDir =  "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";;
        imps = new StringBuilder();
        recordClass = classe.getSimpleName() + "Record";
        embeddableExternalData = EmbeddedExternalData.class.isAssignableFrom(classe);
        adapterSimpleName = classe.getSimpleName() + "Adapter";
        adapterName = classe.getName() + "Adapter";
    }

    public String build() throws IOException {


        StringBuilder classDeclaration = new StringBuilder("\r\n\r\n@NoArgsConstructor\r\n").append("@Service\r\n").append("public class ")
                .append(adapterSimpleName).append(" extends AbstractEntityDTOAdapter<")
                .append(classe.getSimpleName()).append(", ").append(recordClass).append("> {\r\n");

        processImports("mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter",
                "lombok.NoArgsConstructor",
                "mil.decea.mentorpgapi.util.DateTimeAPIHandler",
                "org.springframework.stereotype.Service");


        fileBody.append("\r\n\r\n\t@Override\r\n\tpublic ").append(recordClass)
                .append(" generateRecord() { return new ").append(recordClass)
                .append("(getEntity());}");

        fileBody.append("\r\n\r\n\tpublic ").append(classe.getSimpleName()).append(" updateEntity() {");
        hasAddedField = true;

        for(Method method: classe.getMethods()){
            if (!method.isAnnotationPresent(NotForRecordField.class)) {
                MethodDefaultValue methodDefaultValue = method.getAnnotation(MethodDefaultValue.class);
                try {
                    Field field = getMethodField(method);

                    if (field != null && !Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(NotForRecordField.class)) {
                        CollectionForRecordField ofrf = field.getAnnotation(CollectionForRecordField.class);

                        if (method.getReturnType().isRecord()) {

                        } else if (ofrf != null && ofrf.elementsOfType() != Object.class || Collection.class.isAssignableFrom(field.getType())) {
                            //processAttribute(field,ofrf);
                        } else if (EmbeddedExternalData.class.isAssignableFrom(field.getType())) {
                            processWithExternalDataAdapter(field);
                        } else if (field.isAnnotationPresent(Embedded.class)
                                || field.isAnnotationPresent(ObjectForRecordField.class)
                                || SequenceIdEntity.class.isAssignableFrom(field.getType())) {
                            processWithOtherAdapter(field);
                        } else {
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
            FileWriter arq = new FileWriter(targetDir + adapterSimpleName + ".java");
            arq.write("package " + classe.getPackage().getName() + ";\r\n\r\n");
            arq.write(imps.toString());
            arq.write(classDeclaration.toString());
            arq.write(injections.toString());
            arq.write(fileBody.toString());
            arq.close();
        }
        return adapterName;
    }
    private void processCollectionField(Field field, CollectionForRecordField annotation){

        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();
        String suffixMethod =  fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
        String methodSetName = ".set" + suffixMethod + "(";
        String packRec = annotation.elementsOfType().getName() + "Record";

        checkBrakeLineAndIdentation();

        String toCollection =".toList()";
        String toimport = "java.util.List";

        if (annotation.collectionOfType() != List.class){
            toCollection = ".collect(Collectors.toSet())";
            toimport = "java.util.Set";
        }

        processImports(packRec, toimport);

        String reverseMap = "().stream().map(" + annotation.elementsOfType().getSimpleName() + "::new)" + toCollection + ");";
        fileBody.append("\r\n\t\tgetEntity()").append(methodSetName).append("getIdentifiedRecord().").append(fieldName).append(reverseMap);
        hasAddedField = true;
    }

    private void processWithNewInstance(Method method){

        String name = method.getName().replaceFirst("get","");
        String typeName =  method.getReturnType().getSimpleName() + "Record";
        String packRec = method.getReturnType().getPackage().getName() + "." + typeName;
        String fieldName = name.substring(0, 1).toLowerCase() + name.substring(1);
        String methodSetName = "getEntity().set" + name + "(";


        processImports(packRec);
        checkBrakeLineAndIdentation();
        fileBody.append(methodSetName).append("new ").append(method.getReturnType().getSimpleName()).append("(getIdentifiedRecord().").append(fieldName).append("()));");
        hasAddedField = true;
    }

    private void processWithExternalDataAdapter(Field field){
        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();

        processImports("org.springframework.beans.factory.annotation.Autowired", "mil.decea.mentorpgapi.domain.EmbeddedExternalDataAdapter");
        String objName =  fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        String methodGetName = "getEntity().get" + objName + "()";
        injections.append("\r\n@Autowired\r\n").append("EmbeddedExternalDataAdapter ").append(fieldName).append(";\r\n");
        fileBody.append("\r\n\t\t").append(fieldName).append(".with(").append(methodGetName).append(", ").append("getIdentifiedRecord().").append(fieldName).append("Record()).updateEntity();");
        hasAddedField = true;
    }

    private void processWithOtherAdapter(Field field) throws IOException {

        AdapterBuilder other = new AdapterBuilder(field.getType());
        other.build();
        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();
        String methodGetName = "getEntity().get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1) + "()";

        if (EmbeddedExternalData.class.isAssignableFrom(field.getType())){
            injections.append("\r\n@Autowired\r\n").append("EmbeddedExternalDataAdapter ").append(fieldName).append(";\r\n");
        }else{
            injections.append("\r\n@Autowired\r\n").append(other.adapterSimpleName).append(" ").append(fieldName).append(";\r\n");
        }
        processImports("org.springframework.beans.factory.annotation.Autowired", other.adapterName);
        checkBrakeLineAndIdentation();
        fileBody.append(field.getName()).append(".with(").append(methodGetName).append(", ").append("getIdentifiedRecord().").append(fieldName).append("()).updateEntity();");

        hasAddedField = true;
    }

    private void processAttribute(Method method, Field field){

        String fieldName = field.isAnnotationPresent(RecordFieldName.class) ? field.getAnnotation(RecordFieldName.class).value() : field.getName();

        Class<?> type = method.getReturnType();
        String metodSetName = "getEntity()." + (method.getName().startsWith("get") ? method.getName().replaceFirst("get","set") :
                method.getName().replaceFirst("is","set"))+ "(";

        checkBrakeLineAndIdentation();

        if (LocalDate.class.isAssignableFrom(type) || LocalDateTime.class.isAssignableFrom(type)) {
            processImports("mil.decea.mentorpgapi.util.DateTimeAPIHandler");
            fileBody.append(metodSetName).append("DateTimeAPIHandler.converterStringDate(getIdentifiedRecord().")
                    .append(fieldName).append("()));");
        } else {

            if (!ReflectionUtils.isLikePrimitiveOrString(type)
                    && !type.getPackage().equals(classe.getPackage()) && !impConf.contains(type.getName())) {
                processImports(type.getName());
            }

            if (fieldName.equals("id")) fileBody.append(metodSetName).append("getIdentifiedRecord().").append(fieldName).append("());");
            else fileBody.append(metodSetName).append("getIdentifiedRecord().").append(fieldName).append("());");
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

    private void checkBrakeLineAndIdentation(){
        if (hasAddedField) {
            fileBody.append("\r\n\t\t");
        }
    }

    private void processImports(String... imports){
        for(String i : imports) {
            if (!impConf.contains(i) && !i.startsWith(classe.getPackage().getName())) {
                impConf.add(i);
                imps.append("\r\nimport ").append(i).append(";");
            }
        }
    }

}
