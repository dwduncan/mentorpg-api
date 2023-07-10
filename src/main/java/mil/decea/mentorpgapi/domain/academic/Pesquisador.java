package mil.decea.mentorpgapi.domain.academic;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.SequenceIdEntity;
import mil.decea.mentorpgapi.domain.user.User;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Pesquisador extends SequenceIdEntity {


    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    protected User user;

    @Column
    protected String orcid;
    @Column
    protected String lattes;

}
