package mil.decea.mentorpgapi.etc.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
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
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

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
    public ResponseEntity<?>tratarErroVliadacaoMentor(MentorValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?>tratarErro404(NoHandlerFoundException ex) {
        return ResponseEntity.notFound().build();
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
    public ResponseEntity<?> tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("CPF não autorizado ou senha inválida");
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<?> tokenExpired(JWTVerificationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("A validade da conexão expirou");
    }
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<?> tokenExpired(TokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("A validade da conexão expirou");
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?>tratarErroAcessoNegado(AccessDeniedException ex) {
        String msg = ex.getLocalizedMessage();
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(msg);
    }

    @ExceptionHandler(ClientMinioImplemantationException.class)
    public ResponseEntity<?>tratarErroClientMinioImplemantationException(ClientMinioImplemantationException ex) {
        String msg = ex.getLocalizedMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?>tratarErroConstraintViolationException(ConstraintViolationException ex) {
        boolean b = false;
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            strBuilder.append(b ? "\r\n" : "").append(violation.getMessage());
            b = true;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(strBuilder.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?>tratarErro500(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }

}
