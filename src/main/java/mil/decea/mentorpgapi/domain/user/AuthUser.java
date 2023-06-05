package mil.decea.mentorpgapi.domain.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser implements UserDetails {

    private final Long id;
    private final String cpf;
    private final String senha;
    private final String role;
    private final boolean ativo;
    List<? extends GrantedAuthority> authorities;

    public AuthUser(Long id, String cpf, String role, String senha, boolean ativo) {
        this.id = id;
        this.cpf = cpf;
        this.senha = senha;
        this.ativo = ativo;
        this.role = role;
        this.authorities = role == null || role.isBlank() ? new ArrayList<>() :
                Arrays.stream(role.split("\\s")).map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return ativo;
    }

    @Override
    public boolean isAccountNonLocked() {
        return ativo;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return ativo;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }
}
