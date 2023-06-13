package mil.decea.mentorpgapi.domain.daoservices.repositories;

import mil.decea.mentorpgapi.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByCpf(String cpf);
    List<User> findAllByAtivoTrue(Pageable pageable);

    //@EntityGraph(attributePaths = {"id", "username"})
    //List<User> findAllByAtivoTrue(Pageable pageable);


}