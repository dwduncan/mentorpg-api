package mil.decea.mentorpgapi.domain.daoservices;

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
    static Specification<User> searchUserByNomeCompleto(String search_name){
        return (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            String[] tokens = search_name.trim().toLowerCase(Locale.ROOT).split("\\s+");
            Path<String> p = root.get("nomeCompleto");
            for(String v : tokens) {
                predicates.add(cb.like(cb.function("unaccent",String.class, cb.lower(p)), "%" + StringUtils.stripAccents(v.toLowerCase()) + "%"));
            }
            cq.multiselect(
                    root.get("id"),
                    root.get("ativo"),
                    root.get("posto"),
                    root.get("quadro"),
                    root.get("especialidade"),
                    root.get("nomeGuerra"),
                    root.get("nomeCompleto"));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}