package mil.decea.mentorpgapi.domain.externaldtaio;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractExternalData implements Serializable {

    @Column(columnDefinition = "TEXT")
    protected String formato;
    @Column(columnDefinition = "TEXT")
    protected String nomeArquivo;
    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime dataHoraUpload;
    @Column(columnDefinition = "TEXT")
    private String href = "";

    public void setHref(String href) {
        this.href = href == null ? "" : href;
    }
}
