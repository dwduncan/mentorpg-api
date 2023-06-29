package mil.decea.mentorpgapi.domain.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.datageneration.ObjectForRecordField;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AuthUser implements UserDetails {

    @EqualsAndHashCode.Include
    @NotNull(message = "O id não pode ser nulo")
    private Long id;
    @NotNull(message = "O cpf não pode ser nulo")
    private String cpf;
    @Size(min = 8, message = "A senha deve possuir no mínimo 8 caractéres")
    private String senha;
    private String senhaAntiga;
    private String role;
    private String nome;
    private String descricao;
    private boolean ativo;
    @ObjectForRecordField
    private DataAuthority dataAuthority;
    //private Roles roles;

    @NotForRecordField
    private List<SimpleGrantedAuthority> authorities;

    public AuthUser(Long id, String cpf, String role, String senha, String nome, boolean ativo) {
        this.id = id;
        this.cpf = cpf;
        this.senha = senha;
        this.ativo = ativo;
        this.role = role;
        this.nome = nome;
        this.authorities = role == null || role.isBlank() ? new ArrayList<>() :
                Arrays.stream(role.split("\\s")).map(SimpleGrantedAuthority::new).toList();
    }

    public AuthUser(Long id, String cpf, String role, String senha, String nome, boolean ativo, Posto posto, String quadro, String nomeguerra) {
        this.id = id;
        this.cpf = cpf;
        this.senha = senha;
        this.ativo = ativo;
        this.role = role;
        this.nome = nome;
        this.descricao = createDescriptor(posto,quadro,nomeguerra);
        this.authorities = role == null || role.isBlank() ? new ArrayList<>() :
                Arrays.stream(role.split("\\s")).map(SimpleGrantedAuthority::new).toList();
    }

    private String createDescriptor(Posto posto, String quadro, String nomeguerra){
        if (posto == null) posto = Posto.NIL;
        if (quadro == null) quadro = "";
        return posto.getSigla() + " " + quadro + " " + nomeguerra;
     }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
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

    public void addRole(Roles _role){
        if (role == null || role.isBlank()){
            role = _role.name();
        }else{
            if (!role.contains(_role.name())) role += " " + _role.name();
        }
    }

    public void removeRole(Roles _role){
        if (role != null && role.contains(_role.name())) {
            role = role.replace(_role.name(), "").replaceAll("\\s\\s+"," ").trim();
        }
    }

    public DataAuthority getDataAuthority() {
        if (dataAuthority == null) dataAuthority = new DataAuthority();
        return dataAuthority;
    }


    @NotForRecordField
    public void setAuthUser(AuthUserRecord rec) {
        this.setId(rec.id());
        this.getDataAuthority().setDataAuthority(rec.dataAuthorityRecord());
        this.setRole(rec.role());
        this.setSenhaAntiga(rec.senhaAntiga());
        this.setAtivo(rec.ativo());
        this.setSenha(rec.senha());
        this.setCpf(rec.cpf());
    }

}
