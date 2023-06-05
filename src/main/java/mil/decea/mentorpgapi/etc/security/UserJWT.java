package mil.decea.mentorpgapi.etc.security;

public record UserJWT(Long id,
                      String name,
                      String cpf,
                      String accessToken,
                      Long expirationAtServer,
                      String image,
                      String email,
                      String role) {



}
