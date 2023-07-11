package mil.decea.mentorpgapi.domain.administrativo;

import java.util.Collection;

public interface ControladorOcorrencias<T extends Despacho> {

    Collection<T> getDespachos();

}
