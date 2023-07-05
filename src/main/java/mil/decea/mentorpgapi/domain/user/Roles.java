package mil.decea.mentorpgapi.domain.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

public enum Roles {

    SEMACESSO( "Acesso Não Autorizado"),
    CONVIDADO( "Convidado"),
    ALUNO( "Aluno"),
    PROF( "Professor"),
    REVISOR("Revisor"),
    SECRETARIA( "Secretaria"),
    COORD( "Coordenação"),
    ADMIN( "Administrador"),
    SUPACAD("Supervisor Acadêmico"),
    SUPGERAL("Supervisor Geral");


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

    public Roles convert(String s) {
        return Arrays.stream(Roles.values()).filter(p->roleName.equalsIgnoreCase(s) || p.name().equalsIgnoreCase(s)).findAny().orElse(null);
    }
}
