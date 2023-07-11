package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators;

import mil.decea.mentorpgapi.domain.daoservices.datageneration.CollectionForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.MethodDefaultValue;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors.RecordConstructorProcessor;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors.RecordFieldProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.*;

public class RecordFileGenerator extends AbstractFilesGenerator{

    static Set<String> createdRecords = new HashSet<>();
    Set<String> classFields = new HashSet<>();
    Set<String> requiredImports = new HashSet<>();
    List<String> constructors = new ArrayList<>();
    String recordClass;


    StringBuilder classNameBody;
    public RecordFileGenerator(Class<?> classe) {
        super(classe);
        recordClass = classe.getSimpleName() + "Record";
        init();
    }

    private void init(){

        StringBuilder constructorDeclaration = new StringBuilder("\n\tpublic ")
                .append(recordClass).append("(")
                .append(classe.getSimpleName())
                .append(" obj) {\n\t\tthis(");

        StringBuilder constructor = new StringBuilder(constructorDeclaration);

        classNameBody = new StringBuilder("\npublic record ");
        classNameBody.append(recordClass).append("(\n");

        Map<String,String> emptyFields = new HashMap<>();
        List<String> constructorOrder = new ArrayList<>();

        boolean notFirst = false;
        for(Method method: classe.getMethods()){

            if (!method.isAnnotationPresent(NotForRecordField.class)) {
                MethodDefaultValue methodDefaultValue = method.getAnnotation(MethodDefaultValue.class);
                try {
                    if (methodDefaultValue != null) {
                        if (notFirst) {
                            classNameBody.append(",\n");
                            constructor.append(",\n\t\t\t");
                        }
                        classNameBody.append(method.getReturnType().getSimpleName()).append(" ")
                                .append(methodDefaultValue.fieldName());
                        constructor.append(methodDefaultValue.defaultValue());
                        constructorOrder.add(methodDefaultValue.fieldName());
                        emptyFields.put(methodDefaultValue.fieldName(), "null");
                        notFirst = true;
                    }else{

                        Field field = getMethodField(method);

                        if (field != null &&
                            !Modifier.isStatic(field.getModifiers()) &&
                                !field.isAnnotationPresent(NotForRecordField.class) &&
                                !(Collection.class.isAssignableFrom(field.getType())) &&
                                !field.isAnnotationPresent(CollectionForRecordField.class)){

                            if (notFirst) {
                                classNameBody.append(",\n");
                                constructor.append(",\n\t\t\t");
                            }

                            RecordConstructorProcessor cf = new RecordConstructorProcessor(field);
                            requiredImports.addAll(cf.getRequiredImports());
                            constructor.append(cf.getStatement());

                            RecordFieldProcessor f = new RecordFieldProcessor(field);
                            requiredImports.addAll(f.getRequiredImports());
                            classNameBody.append(f.getStatement());
                            emptyFields.put(field.getName(), getConstructorValue(field.getType()));
                            constructorOrder.add(field.getName());
                            notFirst = true;
                        }

                    }
                } catch (Exception ignored) {
                }
            }
        }

        constructor.append("\n\t\t);\n\t}");

        for(Constructor<?> cttr : classe.getDeclaredConstructors()) {
            if (!cttr.isAnnotationPresent(NotForRecordField.class) && cttr.getParameterCount() > 0) {

                StringBuilder otherConstructor = new StringBuilder("\n\tpublic ")
                        .append(recordClass).append("(");

                notFirst = false;
                for(Parameter p : cttr.getParameters()){
                    if (notFirst)  otherConstructor.append(", ");
                    otherConstructor.append(p.getType().getSimpleName()).append(" ").append(p.getName());
                    notFirst = true;
                }

                otherConstructor.append(") {\n\n\t\tthis(");

                List<String> _paramsNames = Arrays.stream(cttr.getParameters()).map(Parameter::getName).toList();

                notFirst = false;
                for(String fName : constructorOrder){
                    if (notFirst)  otherConstructor.append(",\n\t\t\t");
                    if (_paramsNames.contains(fName)) otherConstructor.append(fName);
                    else otherConstructor.append(emptyFields.get(fName));
                    notFirst = true;
                }

                otherConstructor.append("\n\t\t);\n\t}");

                constructor.append(otherConstructor);
            }
        }

        requiredImports.add("mil.decea.mentorpgapi.domain.IdentifiedRecord");
        requiredImports.add(classe.getName());

        classNameBody.append(") implements IdentifiedRecord {\n");

        constructors.add(constructor.toString());
    }



    @Override
    public String getClassNameStatement() {
        return classNameBody.toString();
    }

    @Override
    public String getSimpleFileName() {
        return recordClass + ".java";
    }

    @Override
    public List<String> getMethods() {
        return new ArrayList<>();
    }

    @Override
    public List<String> getConstructors() {
        return constructors;
    }

    @Override
    public Set<String> getRequiredImports() {
        return requiredImports;
    }

    @Override
    public Set<String> getClassFields() {
        return classFields;
    }

    @Override
    public String getTargetDir() {
        return "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/records/";
    }


    private void checkOrCreateRecordForField(RecordFieldProcessor field)  {

        if (field.isRequestNewRecord()){
            Class<?> _classe = field.getField().getType();
            String arq = "./src/main/java/" + _classe.getName().replaceAll("\\.","/") + "Record.java";
            File _file = new File(arq);
            System.out.println("Criando " + arq);
            if (!_file.exists() && createdRecords.add(arq)) {
                try {
                    new RecordFileGenerator(_classe).createFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

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

    private String getConstructorValue(Class<?> _classe){

        if (_classe.isPrimitive()){
            return boolean.class.isAssignableFrom(_classe) ? "false" : "0";
        }

        return "null";
    }
}
