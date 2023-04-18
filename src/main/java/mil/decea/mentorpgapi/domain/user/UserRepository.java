package mil.decea.mentorpgapi.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByCpf(String cpf);

}
