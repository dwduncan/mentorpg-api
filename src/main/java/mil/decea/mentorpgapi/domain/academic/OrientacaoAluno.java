package mil.decea.mentorpgapi.domain.academic;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.user.User;

import java.io.Serial;

@Table(name = "orientacoesalunos", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class OrientacaoAluno extends Mensagem<User> {

    @Serial
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receptor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private RelatorioAcademico relatorio;

    @Override
    public User getReceptor() {
        return receptor;
    }

    @Override
    public void setReceptor(User receptor) {
        this.receptor = receptor;
    }

    @Override
    public String getEntityDescriptor() {
        return "Mensagem enviada por " + getUsuarioEmissor().getNomeQualificado() ;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
