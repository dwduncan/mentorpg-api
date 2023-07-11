package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.academic.ConsultaOrientador;

import java.util.HashSet;
import java.util.Set;

@Table(name = "despachosconsultaorientadores", schema = "mentorpgapi")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class DespachoConsultaOrientacao extends Despacho {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ConsultaOrientador consultaOrientador;
}
