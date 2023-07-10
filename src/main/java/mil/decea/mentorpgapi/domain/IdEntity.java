package mil.decea.mentorpgapi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.changewatch.trackdefiners.IgnoreTrackChange;

import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class IdEntity {

    @IgnoreTrackChange
    @Column(columnDefinition = "TIMESTAMP")
    protected LocalDateTime lastUpdate;
    boolean ativo = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IdEntity sequenceIdEntity)) return false;
        return Objects.equals(getId(), sequenceIdEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public abstract Long getId();

    public boolean isAtivo() {
        return this.ativo;
    }

    public java.time.LocalDateTime getLastUpdate() {
        return this.lastUpdate;
    }

    public abstract void setId(Long id);

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setLastUpdate(java.time.LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
