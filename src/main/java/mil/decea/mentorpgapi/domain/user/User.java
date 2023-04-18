package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.user.dto.UserDTO;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings({"JpaAttributeMemberSignatureInspection", "JpaAttributeTypeInspection"})
@Table(name = "users", schema = "mentorpgapi",indexes = {@Index(unique = true, name = "indexcpfs", columnList = "cpf")})
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails {


    @IsValidCpf
    @Column(columnDefinition = "TEXT", nullable = false)
    private String cpf;
    @Column @Enumerated(EnumType.STRING)
    private Titulacao titulacao;
    @Column @Enumerated(EnumType.STRING)
    private Posto posto;
    @Column(columnDefinition = "TEXT")
    private String quadro;
    @Column(columnDefinition = "TEXT")
    private String especialidade;
    @NotNull(message = "Informe o nome de guerra")
    @Column(columnDefinition = "TEXT", nullable = false)
    private String nomeGuerra;
    @Column(columnDefinition = "TEXT", nullable = false)
    @NotNull(message = "Informe o nome completo")
    private String nomeCompleto;
    @Enumerated(EnumType.ORDINAL)
    private Sexo sexo = Sexo.MASCULINO;
    @Enumerated(EnumType.ORDINAL)
    private ForcaSingular forcaSingular;
    @Column(columnDefinition = "DATE")
    private LocalDate ultimaPromocao;
    private boolean pttc = false;
    private int antiguidadeRelativa;
    @Column(columnDefinition = "TEXT")
    private String senha;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String roles = "";
    @Embedded
    private Endereco endereco;
    @Embedded
    private Contato contato;
    @Embedded
    private UserImage photo;
    @Column(columnDefinition = "TEXT")
    private String identidade;
    @Column(columnDefinition = "TEXT")
    private String passaporte;
    @Column(columnDefinition = "TEXT")
    private String nacionalidade;
    @Column(columnDefinition = "TEXT")
    private String naturalidade;
    @Column(columnDefinition = "DATE")
    private LocalDate dataNascimento;
    @Column(columnDefinition = "DATE")
    private LocalDate validadePassaporte;
    @Column(columnDefinition = "DATE")
    private LocalDate dataPraca;
    @Column(columnDefinition = "DATE")
    private LocalDate proximaPromocao;
    @Column(columnDefinition = "TEXT")
    private  String banco;
    @Column(columnDefinition = "TEXT")
    private String agencia;
    @Column(columnDefinition = "TEXT")
    private String conta;
    @Column(columnDefinition = "TEXT")
    private String saram;
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDocument> documents = new ArrayList<>();


    public User(UserDTO dto){
        //todo
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
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

    public void setRoles(String roles) {
        this.roles = roles != null ? roles.trim() : "";
    }
}
