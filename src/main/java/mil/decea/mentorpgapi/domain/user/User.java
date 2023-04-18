package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import mil.decea.mentorpgapi.domain.BaseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("JpaAttributeMemberSignatureInspection")
@Table(name = "usuarios", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {


    private String cpf;
    private String senha;
    private String roles = "";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {

    }


    @Override
    public String getPassword() {
        return senha;
    }

    public void setPassword(String password){
        senha = password;
    }

    @Override
    public String getUsername() {
        return cpf;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
