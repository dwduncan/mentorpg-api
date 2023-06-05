package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.BaseEntity;
import mil.decea.mentorpgapi.domain.NotForRecordField;
import mil.decea.mentorpgapi.domain.daoservices.minio.MinioStorage;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import mil.decea.mentorpgapi.util.DateTimeAPIHandler;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Table(name = "users", schema = "mentorpgapi",indexes = {@Index(unique = true, name = "indexcpfs", columnList = "cpf")})
@Entity
@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity implements UserDetails, MinioStorage<UserImage> {


    @IsValidCpf
    @NotNull(message = "Informe um CPF válido")
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
    @NotForRecordField
    @Column(columnDefinition = "TEXT")
    private String senha;
    @Column(columnDefinition = "TEXT")
    private String role;
    @Column(columnDefinition = "TEXT")
    private String celular;
    @Column(columnDefinition = "TEXT")
    private String email;
    @Embedded
    private UserImage userImage;
    @Column(columnDefinition = "TEXT")
    private String identidade;
    @Column(columnDefinition = "DATE")
    private LocalDate dataNascimento;
    @Column(columnDefinition = "DATE")
    private LocalDate dataPraca;
    @Column(columnDefinition = "DATE")
    private LocalDate proximaPromocao;
    @Column(columnDefinition = "TEXT")
    private String saram;
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserDocument> documents = new ArrayList<>();
    public User(Long id,
                boolean ativo,
                Posto posto,
                String quadro,
                String especialidade,
                String nomeGuerra,
                String nomeCompleto) {

        super(id, ativo);
        this.posto = posto;
        this.quadro = quadro;
        this.especialidade = especialidade;
        this.nomeGuerra = nomeGuerra;
        this.nomeCompleto = nomeCompleto;
    }

    public User(Long id,
                boolean ativo,
                @NotNull(message = "Informe um CPF válido") String cpf,
                Titulacao titulacao,
                Posto posto,
                String quadro,
                String especialidade,
                @NotNull(message = "Informe o nome de guerra") String nomeGuerra,
                @NotNull(message = "Informe o nome completo") String nomeCompleto,
                Sexo sexo,
                boolean pttc) {

        super(id, ativo);
        this.cpf = cpf;
        this.titulacao = titulacao;
        this.posto = posto;
        this.quadro = quadro;
        this.especialidade = especialidade;
        this.nomeGuerra = nomeGuerra;
        this.nomeCompleto = nomeCompleto;
        this.sexo = sexo;
        this.pttc = pttc;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null || role.isBlank()) return new ArrayList<>();
        String[] _roles = role.split("\\s");
        return Arrays.stream(_roles).map(SimpleGrantedAuthority::new).toList();
    }

    public void setCpf(String cpf) {
        this.cpf = cpf.replaceAll("\\D","") ;
    }

    public UserImage getUserImage(){
        if (userImage == null) userImage = new UserImage();
        return userImage;
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
        return isAtivo();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAtivo();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isAtivo();
    }

    @Override
    public boolean isEnabled() {
        return isAtivo();
    }
    @NotForRecordField
    public void setUser(UserRecord rec) {
        this.setAntiguidadeRelativa(rec.antiguidadeRelativa());
        this.getUserImage().setUserImage(rec.userImageRecord());
        this.setNomeCompleto(rec.nomeCompleto());
        this.setEspecialidade(rec.especialidade());
        this.setTitulacao(rec.titulacao());
        this.setPosto(rec.posto());
        this.setCpf(rec.cpf());
        this.setQuadro(rec.quadro());
        this.setNomeGuerra(rec.nomeGuerra());
        this.setSexo(rec.sexo());
        this.setObservacoes(rec.observacoes());
        this.setCelular(rec.celular());
        this.setDataNascimento(DateTimeAPIHandler.converterStringDate(rec.dataNascimento()));
        this.setPttc(rec.pttc());
        this.setRole(rec.role());
        this.setIdentidade(rec.identidade());
        this.setProximaPromocao(DateTimeAPIHandler.converterStringDate(rec.proximaPromocao()));
        this.setUltimaPromocao(DateTimeAPIHandler.converterStringDate(rec.ultimaPromocao()));
        this.setDataPraca(DateTimeAPIHandler.converterStringDate(rec.dataPraca()));
        this.setSaram(rec.saram());
        this.setEmail(rec.email());
        this.setForcaSingular(rec.forcaSingular());
        //this.setDocuments(rec.documents());
        this.setId(rec.id());
        this.setAtivo(rec.ativo());
    }

    @NotForRecordField
    @Override
    public String getBucket() {
        return "userimage";
    }
    @NotForRecordField
    @Override
    public String getStorageDestinationPath() {
        return getCpf() + "/photo" ;
    }//+ getUserImage().getNomeArquivo()

    @Override
    @NotForRecordField
    public UserImage getExternalData() {
        return getUserImage();
    }
}
