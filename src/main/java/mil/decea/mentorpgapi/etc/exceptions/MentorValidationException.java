package mil.decea.mentorpgapi.etc.exceptions;

public class MentorValidationException extends RuntimeException {
    public MentorValidationException(String mensagem) {
        super(mensagem);
    }
}
