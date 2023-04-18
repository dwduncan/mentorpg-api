package mil.decea.mentorpgapi.apisupport.exceptions;

public class MentorValidationException extends RuntimeException {
    public MentorValidationException(String mensagem) {
        super(mensagem);
    }
}
