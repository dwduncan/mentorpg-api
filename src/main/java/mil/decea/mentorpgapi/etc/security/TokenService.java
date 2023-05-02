package mil.decea.mentorpgapi.etc.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import mil.decea.mentorpgapi.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final String ISSUER = "MentorPG-API";

    private final int TEMPO_EXPIRACAO_MINUTOS = 30;

    @Value("${mentorapi.security.token.secret}")
    private String secret;

    public DataTokenJWT gerarToken(User usuario) {

        try {

            var algoritmo = Algorithm.HMAC256(secret);
            var expireAt = expireAt();

            String token = JWT.create()
                .withIssuer(ISSUER)
                .withSubject(usuario.getCpf())
                .withExpiresAt(expireAt())
                .withClaim("roles", usuario.getRoles())
                .sign(algoritmo);

            return new DataTokenJWT(usuario.getId(),
                    usuario.getNomeCompleto(),
                    token,
                    expireAt.toEpochMilli(),
                    usuario.getPhoto().getHref(),
                    usuario.getContato().getDefautEmail(),
                    usuario.getRoles());

        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant expireAt() {
        return LocalDateTime.now().plusMinutes(TEMPO_EXPIRACAO_MINUTOS).toInstant(ZoneOffset.of("-03:00"));
    }

}
