package mil.decea.mentorpgapi.domain.daoservices;

public interface DTOValidator<T> {
    void validate(T dto);
}
