package mil.decea.mentorpgapi.domain.daoservices;

import mil.decea.mentorpgapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCpf(String cpf);

}
