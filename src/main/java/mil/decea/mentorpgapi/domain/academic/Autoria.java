package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.user.User;

import java.io.Serial;
import java.util.Objects;


@Table(name = "autorias", schema = "mentorpgapi")
@IdClass(AutoriaPK.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Autoria<T extends AbstractPublicacao<T>> implements Autor, Comparable<Autoria<?>>{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected User usuario;

    @Id
    @NotNull
    protected Long idPublicacao;

    @NotNull
    protected String publicationClassName;

    protected boolean autorPrincipal;

    @Transient
    protected T publicacao;

    public Autoria(@NotNull User usuario, @NotNull T publicacao) {
        this.usuario = usuario;
        setPublicacao(publicacao);
    }

    public Autoria(@NotNull User usuario, @NotNull T publicacao, boolean autorPrincipal) {
        this.usuario = usuario;
        setPublicacao(publicacao);
        this.autorPrincipal = autorPrincipal;
    }

    public void setPublicacao(T publicacao){
        this.idPublicacao = publicacao.getId();
        this.publicationClassName = publicacao.getClass().getName();
        this.publicacao = publicacao;
    }

    @Override
    public int compareTo(@NotNull Autoria autoria) {
        int d = usuario.compareTo(autoria.usuario);
        if (d == 0){
            d = publicacao.getTitulo().compareToIgnoreCase(autoria.publicacao.getTitulo());
        }
        return d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Autoria<?> autoria = (Autoria<?>) o;
        return Objects.equals(usuario, autoria.usuario) && Objects.equals(idPublicacao, autoria.idPublicacao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, idPublicacao);
    }

    @Override
    public T getPublicacao() {
        return publicacao;
    }
}
