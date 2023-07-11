package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPublicacao<T extends AbstractPublicacao<T>> extends PublicationDataEntity implements Comparable<AbstractPublicacao> {

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoPublicacao tipoPublicacao;

    @Column(columnDefinition = "TEXT")
    private String titulo = "";
    private String issn;
    private String doi;
    private String qualis;
    private String linkPublicacao;
    private String veiculoDaPublicacao;
    private LocalDate mesAnoPublicacao;
    private double creditos;
    @Column(columnDefinition = "TEXT")
    private String palavrasChave;


    public abstract List<Autoria<T>> getAutores();


    public void setTitulo(String titulo) {
        this.titulo = titulo != null ? titulo : "";
    }

    public String getTipoPublicacaoString(){
        return getTipoPublicacao() == null ? "" : getTipoPublicacao().getTipo();
    }

    public String getPublicadoEm(){
        return getMesAnoPublicacao() != null ? " (publicado em " + getMesAnoPublicacao().format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")" : "";
    }

    public double getCreditos() {
        return creditos;
    }

    public void setCreditos(double creditos) {
        this.creditos = creditos;
    }

    public String getCreditosFormatado(){
        return new DecimalFormat("0.00").format(getCreditos());
    }

    public boolean isPossuiTitulo(){
        return titulo != null && !titulo.isBlank();
    }


    public String getRotuloNomeDoArquivo(){
        return nomeArquivo != null ? nomeArquivo : "<O documento não foi carregado>";
    }

    public String getNomesAutores(){
        StringBuilder nomes = new StringBuilder();
        for(Autoria<?> u : getAutores()){
            if (nomes.length() == 0) nomes = new StringBuilder(u.getUsuario().getNomeQualificado());
            else nomes.append(", ").append(u.getUsuario().getNomeQualificado());
        }
        return nomes.toString();
    }

    public String getVersaoImpressao(){
        String data = "";
        String veiculo = veiculoDaPublicacao != null && !veiculoDaPublicacao.isBlank() ? veiculoDaPublicacao + " " : "";
        if (getMesAnoPublicacao() != null) data = " (publicado em " + getMesAnoPublicacao().format(DateTimeFormatter.ofPattern("MM/yyyy")) + ")";
        return veiculo + getTitulo() + data;
    }

    public String getDadosComplementaresImpressao(){
        String autores = " autores: " + getNomesAutores();
        String creds = creditos > 0 ? " (" + creditos + " créditos)" : "";
        String link = linkPublicacao != null && !linkPublicacao.isBlank() ? " <" + linkPublicacao + ">" : "";
        return getTipoPublicacao().getTipo() + autores + link + creds;
    }
    @Override
    public AbstractPublicacao clone() {
        return (AbstractPublicacao) super.clone();
    }



    @Override
    public int compareTo(AbstractPublicacao o) {
        return getTitulo().compareToIgnoreCase(o.getTitulo());
    }
}
