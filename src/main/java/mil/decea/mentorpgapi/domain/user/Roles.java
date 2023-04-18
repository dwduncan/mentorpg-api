package mil.decea.mentorpgapi.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Roles {

    USER("USER"),
    ADMIN("ADMIN"),
    COORDENADOR("COORDENADOR"),
    VISITANTE("VISITANTE"),
    SUPERVISOR("SUPERVISOR"),
    PROFESSOR("PROFESSOR"),
    ALUNO("ALUNO");


    private String roleName;
    private GrantedAuthority grantedAuthority;

    Roles(String role){
        roleName = role;
        grantedAuthority = new SimpleGrantedAuthority(getRoleName());
    }


    public String getRoleName() {
        return roleName;
    }

    public GrantedAuthority getGrantedAuthority() {
        return grantedAuthority;
    }
}
