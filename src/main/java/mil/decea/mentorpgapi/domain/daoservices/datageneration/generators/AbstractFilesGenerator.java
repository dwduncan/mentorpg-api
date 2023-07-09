package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public abstract class AbstractFilesGenerator {

    protected final Class<?> classe;
    protected String targetDir;
    protected StringBuilder fileBody = new StringBuilder();

    String classCloseBlock = "\n}";

    public AbstractFilesGenerator(Class<?> classe) {
        this.classe = classe;
        this.targetDir =  "./src/main/java/" + classe.getPackage().getName().replaceAll("\\.","/") + "/";
    }

    public String getPackageStatement(){
        return "package " + classe.getPackageName() + ";";
    }
    public abstract String getClassNameStatement();
    public abstract String getSimpleFileName();
    public abstract List<String> getMethods();
    public abstract List<String> getConstructors();
    public abstract Set<String> getRequiredImports();
    public abstract Set<String> getClassFields();

    public void createFile() throws IOException {

        fileBody.append(getPackageStatement());

        if (getPackageStatement() != null && !getPackageStatement().isEmpty()) fileBody.append("\n\n");

        for(String imp : getRequiredImports()){
            fileBody.append("import ").append(imp).append(";\n");
        }

        if (!getRequiredImports().isEmpty()) fileBody.append("\n");

        fileBody.append(getClassNameStatement()).append("\n");

        for(String inj : getClassFields()){
            fileBody.append(inj).append(";\n");
        }

        if (!getClassFields().isEmpty()) fileBody.append("\n");

        for(String c : getConstructors()){
            fileBody.append(c).append("\n");
        }

        if (getConstructors().isEmpty()) fileBody.append("\n");


        for(String m : getMethods()){
            fileBody.append(m).append(";\n");
        }

        if (!getMethods().isEmpty()) fileBody.append("\n");

        fileBody.append(classCloseBlock);

        if (!fileBody.toString().isBlank()) {
            if (!targetDir.endsWith("/")) targetDir += "/";
            FileWriter arq = new FileWriter(targetDir + getSimpleFileName());
            arq.write(fileBody.toString());
            arq.close();
        }
    }

}
