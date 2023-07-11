package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators;

import mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors.ObjectFieldProcessor;
import mil.decea.mentorpgapi.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdapterFilesGenerator extends AbstractFilesGenerator{
    Set<String> injections = new HashSet<>();
    Set<String> requiredImports = new HashSet<>();
    List<ObjectFieldProcessor> fields;
    List<String> methods = new ArrayList<>(2);
    String recordClass;
    public AdapterFilesGenerator(Class<?> classe) {
        super(classe);
        recordClass = classe.getSimpleName() + "Record";
        init();
    }

    private void init(){
        Set<Field> _fields = ReflectionUtils.getAllFields(classe, false, null);
        fields = _fields.stream().map(ObjectFieldProcessor::new).toList();

        requiredImports.addAll(List.of("mil.decea.mentorpgapi.domain.AbstractEntityDTOAdapter",
                classe.getName(),
                "lombok.NoArgsConstructor",
                "org.springframework.stereotype.Service"));
        
        StringBuilder method1 = new StringBuilder();
        StringBuilder method2 = new StringBuilder();

        method1.append("\n\n\t@Override\n\tpublic ").append(recordClass)
                .append(" generateRecord() { return new ").append(recordClass)
                .append("(getEntity());}");
        
        method2.append("\n\n\tpublic ").append(classe.getSimpleName()).append(" updateEntity() {");
        fields.forEach(f -> {                   
            String statement = f.getStatement();
            if (statement != null){
                requiredImports.addAll(f.getRequiredImports());
                injections.addAll(f.getInjections());
                method2.append("\n\t\t").append(statement).append(";");
            }
        });

        method2.append("\n\t\treturn getEntity();\n\t}");
    }

    @Override
    public String getClassNameStatement() {
        return "\n\n@NoArgsConstructor\n@Service\npublic class " + classe.getSimpleName() + "Adapter " +
                " extends AbstractEntityDTOAdapter<" + classe.getSimpleName() + ", " + classe.getSimpleName()
                + "Record> {\n";
    }

    @Override
    public String getSimpleFileName() {
        return classe.getSimpleName() + "Adapter.java";
    }

    @Override
    public List<String> getMethods() {
        return methods;
    }

    @Override
    public List<String> getConstructors() {
        return new ArrayList<>();
    }

    @Override
    public Set<String> getRequiredImports() {
        return requiredImports;
    }

    @Override
    public Set<String> getClassFields() {
        return injections;
    }


    @Override
    public String getTargetDir() {
        return "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/adapters/";
    }

}
