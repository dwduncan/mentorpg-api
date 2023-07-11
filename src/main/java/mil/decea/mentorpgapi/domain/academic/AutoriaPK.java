package mil.decea.mentorpgapi.domain.academic;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.user.User;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Getter
@Setter
@NoArgsConstructor
public class AutoriaPK implements Serializable, Comparable<AutoriaPK> {

    @Serial
    private static final long serialVersionUID = 1L;

    protected User usuario;

    protected Long publicacaoId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AutoriaPK autoriaPK = (AutoriaPK) o;
        return Objects.equals(usuario, autoriaPK.usuario) && Objects.equals(publicacaoId, autoriaPK.publicacaoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usuario, publicacaoId);
    }

    @Override
    public int compareTo(@NotNull AutoriaPK autoriaPK) {
        int d = usuario.compareTo(autoriaPK.usuario);
        if (d == 0){
            d = publicacaoId.compareTo(autoriaPK.getPublicacaoId());
        }
        return d;
    }
}
