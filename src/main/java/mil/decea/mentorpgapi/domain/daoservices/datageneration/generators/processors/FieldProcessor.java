package mil.decea.mentorpgapi.domain.daoservices.datageneration.generators.processors;

import java.util.Set;

public interface FieldProcessor {

    String processorName();

    String getStatement();

    Set<String> getRequiredImports();

}
