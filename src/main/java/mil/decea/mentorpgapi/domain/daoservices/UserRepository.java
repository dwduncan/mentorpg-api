package mil.decea.mentorpgapi.domain.daoservices;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import mil.decea.mentorpgapi.domain.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findByCpf(String cpf);
    List<User> findAllByAtivoTrue(Pageable pageable);

    static Specification<User> searchUserByNomeCompleto(String search_name){
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            String[] tokens = search_name.trim().toLowerCase(Locale.ROOT).split("\\s+");
            Path<String> p = root.get("nomeCompleto");
            for(String v : tokens) {
                predicates.add(cb.like(cb.function("unaccent",String.class, cb.lower(p)), "%" + StringUtils.stripAccents(v.toLowerCase()) + "%"));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
