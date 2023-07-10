package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public abstract class Pesquisa extends SequenceIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private AreaDePesquisa areaPesquisa;
    @ManyToOne(fetch = FetchType.LAZY)
    private LinhaDePesquisa linhaDePesquisa;
    @ManyToOne(fetch = FetchType.LAZY)
    private ProgramaPosGraduacao programa;
    @ManyToOne(fetch = FetchType.LAZY)
    private AreaDeConcentracao areaConcentracao;

    @ManyToOne
    private Professor orientador;
    @ManyToOne
    private Professor coorientador;

    @Column(columnDefinition = "TEXT")
    private String tema;

    @Column(columnDefinition = "TEXT")
    private String titulo;


}
