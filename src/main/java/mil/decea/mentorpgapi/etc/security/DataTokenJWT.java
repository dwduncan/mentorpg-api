package mil.decea.mentorpgapi.etc.security;

public record DataTokenJWT(Long id, String username, String access_token, Long expiredAt, String avatar) {}
