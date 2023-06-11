package mil.decea.mentorpgapi.etc.exceptions;

public class MentorTokenException extends Exception{

    public MentorTokenException() {
    }

    public MentorTokenException(String message) {
        super(message);
    }

    public MentorTokenException(Throwable cause) {
        super(cause);
    }
}
