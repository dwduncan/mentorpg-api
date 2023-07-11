package mil.decea.mentorpgapi.domain.administrativo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.IdEntity;
import mil.decea.mentorpgapi.domain.TrackedEntity;
import mil.decea.mentorpgapi.domain.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "despachos", schema = "mentorpgapi")
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Despacho extends IdEntity implements Comparable<Despacho>, TrackedEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "DESPACHOS_SEQ", sequenceName = "DESPACHOS_SEQ", allocationSize = 1)
    protected Long id;

    @NotNull(message = "Informe a ocorrência ou despacho")
    @Column(columnDefinition = "TEXT")
    private String ocorrencia;
    private boolean eventoParaTimeLine = true;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime realizadoEm;
    @ManyToOne
    private User realizadoPor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "despacho")
    private Set<AnexoDespacho> anexos;

    public LocalDateTime getRealizadoEm() {
        if (realizadoEm == null) realizadoEm = LocalDateTime.now();
        return realizadoEm;
    }

    public String getRealizadoEmFormatado() {
        return getRealizadoEm().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Set<AnexoDespacho> getAnexos(){
        if (anexos == null) anexos = new HashSet<>();
        return anexos;
    }

    public String getDataHoraFormatada(){
        return realizadoEm != null ? realizadoEm.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "";
    }

    public String getRealizadoPorFormatado(){
        return realizadoPor != null ? realizadoPor.getNomeQualificado() : "";
    }

    @Override
    public int compareTo(Despacho o) {
        return getRealizadoEm().compareTo(o.getRealizadoEm());
    }

    public boolean isPossuiAnexos(){
        return getAnexos().isEmpty();
    }

    public void adicionarAnexo(AnexoDespacho doc){
        getAnexos().add(doc);
    }

    public int getTotalAnexos(){
        return getAnexos().size();
    }

    @Override
    public String getEntityDescriptor() {
        return "Despacho - " + ocorrencia;
    }

    @Override
    public TrackedEntity getParentObject() {
        return null;
    }
}
