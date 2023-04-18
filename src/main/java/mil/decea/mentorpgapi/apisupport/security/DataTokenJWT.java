package mil.decea.mentorpgapi.apisupport.security;

public record DataTokenJWT(Long id, String username, String access_token, Long expiredAt, String avatar) {}
