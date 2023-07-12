package mil.decea.mentorpgapi.util.datageneration.generators.processors;

import java.util.Set;

public interface FieldProcessor {

    String processorName();

    String getStatement();

    Set<String> getRequiredImports();

}
