package mil.decea.mentorpgapi.domain.daoservices.repositories;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import mil.decea.mentorpgapi.domain.user.Posto;
import mil.decea.mentorpgapi.domain.user.Sexo;
import mil.decea.mentorpgapi.domain.user.Titulacao;
import mil.decea.mentorpgapi.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByCpf(String cpf);
    List<User> findAllByAtivoTrue(Pageable pageable);

    //@EntityGraph(attributePaths = {"id", "username"})
    //List<User> findAllByAtivoTrue(Pageable pageable);


}