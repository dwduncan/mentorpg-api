package mil.decea.mentorpgapi.etc.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?>tratarErro404() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?>tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        StringBuilder sb = new StringBuilder();
        boolean b = false;
        for(FieldError fe : erros){
            if (b) sb.append("\r\n");
            sb.append(fe.getDefaultMessage());
            b = true;
        }
        return ResponseEntity.badRequest().body(sb.toString());
    }

    @ExceptionHandler(MentorValidationException.class)
    public ResponseEntity<?>tratarErroVliadacaoMentor(Exception ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?>tratarErro400(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?>tratarErroBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?>tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("CPF não autorizado ou senha inválida");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?>tratarErroAcessoNegado(AccessDeniedException ex) {
        String msg = ex.getLocalizedMessage();
        System.out.println(msg);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?>tratarErro500(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }
}
