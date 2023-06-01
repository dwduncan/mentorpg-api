package mil.decea.mentorpgapi.domain.user;

import mil.decea.mentorpgapi.domain.EnumConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public enum Roles implements EnumConverter<Roles> {

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

    @Override
    public Roles convert(String s) {
        return Arrays.stream(Roles.values()).filter(p->roleName.equalsIgnoreCase(s) || p.name().equalsIgnoreCase(s)).findAny().orElse(null);
    }
}
