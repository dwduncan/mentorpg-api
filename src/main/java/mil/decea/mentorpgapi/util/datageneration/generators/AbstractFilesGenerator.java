package mil.decea.mentorpgapi.util.datageneration.generators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public abstract class AbstractFilesGenerator {

    protected final Class<?> classe;

    protected StringBuilder fileBody = new StringBuilder();

    String classCloseBlock = "\n}";

    public AbstractFilesGenerator(Class<?> classe) {
        this.classe = classe;
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
    public abstract String getTargetDir();

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
            String targetDir = getTargetDir();
            if (!targetDir.endsWith("/")) targetDir += "/";
            Path path = Paths.get(targetDir);
            if (!path.toFile().exists()) Files.createDirectory(path);
            FileWriter arq = new FileWriter(targetDir + getSimpleFileName());
            arq.write(fileBody.toString());
            arq.close();
        }
    }

}
