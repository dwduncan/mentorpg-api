package mil.decea.mentorpgapi.domain;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public interface DomainEntity  {
    Long getId();

    java.time.LocalDateTime getLastUpdate();

    void setId(Long id);

    void setLastUpdate(java.time.LocalDateTime lastUpdate);
}
