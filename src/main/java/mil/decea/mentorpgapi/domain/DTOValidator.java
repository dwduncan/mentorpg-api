package mil.decea.mentorpgapi.domain;

public interface DTOValidator<T> {
    void validate(T dto);
}
