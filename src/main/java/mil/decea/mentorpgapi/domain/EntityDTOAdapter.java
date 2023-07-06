package mil.decea.mentorpgapi.domain;

import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;

public interface EntityDTOAdapter<E extends DomainEntity, R extends IdentifiedRecord> {

    E getEntity();
    R getIdentifiedRecord();
    ObjectChangesChecker getChangesAndUpdate();
    R generateRecord();
    E updateEntity();
    void setIdentifiedRecord(R rec);

    void setEntity(E e);

    /**
     * Permite inserir a entidade e o dto em casos no qual o adaptador é injetado.  O método retorna
     * a própria instancia onde está declarado.
     * @param e
     * @param r
     * @return
     */
    default EntityDTOAdapter<E,R> with(E e, R r){
        setIdentifiedRecord(r);
        setEntity(e);
        return this;
    }
}
