package mil.decea.mentorpgapi.etc.security;

public record DataTokenJWT(Long id,
                           String name,
                           String accessToken,
                           Long expirationAtServer,
                           String image,
                           String email,
                           String role) {}
