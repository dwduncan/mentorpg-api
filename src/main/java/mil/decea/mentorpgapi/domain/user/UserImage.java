package mil.decea.mentorpgapi.domain.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mil.decea.mentorpgapi.domain.externaldtaio.AbstractExternalData;
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class UserImage extends AbstractExternalData {
}
