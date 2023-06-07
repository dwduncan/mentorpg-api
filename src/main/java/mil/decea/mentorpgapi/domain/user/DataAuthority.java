package mil.decea.mentorpgapi.domain.user;

import lombok.*;
import mil.decea.mentorpgapi.domain.NotForRecordField;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DataAuthority implements Serializable {

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String cpf;


    @NotForRecordField
    public void setDataAuthority(DataAuthorityRecord rec) {
        this.setName(rec.name());
        this.setId(rec.id());
        this.setCpf(rec.cpf());
    }

}
