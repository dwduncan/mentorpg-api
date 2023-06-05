package mil.decea.mentorpgapi.domain.daoservices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import mil.decea.mentorpgapi.domain.DTOValidator;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClienteMinio;
import mil.decea.mentorpgapi.domain.daoservices.repositories.UserRepository;
import mil.decea.mentorpgapi.domain.user.AuthUser;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import mil.decea.mentorpgapi.domain.user.dto.UserDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final List<DTOValidator<UserDTO>> validators;
    private final EntityManager entityManager;

    private final ClienteMinio clienteMinio;
    @Autowired
    public UserService( UserRepository repository,
                        List<DTOValidator<UserDTO>> validators,
                        EntityManager entityManager,
                        ClienteMinio clienteMinio) {
        this.repository = repository;
        this.validators = validators;
        this.entityManager = entityManager;
        this.clienteMinio = clienteMinio;
    }
    public AuthUser getAuthUser(String cpf){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<AuthUser> cq = cb.createQuery(AuthUser.class);
        Root<User> root = cq.from(User.class);

        cq.multiselect(
                        root.get("id"),
                        root.get("cpf"),
                        root.get("role"),
                        root.get("senha"),
                        root.get("ativo"))
                .where(cb.equal(root.get("cpf"),cpf));

        try{
            return  entityManager.createQuery(cq).getSingleResult();
        }catch (NoResultException ex){
            return null;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        User usr = repository.findByCpf(cpf.replaceAll("\\D",""));
        if (usr != null) {
            try {
                clienteMinio.insertSasUrl(usr);
            } catch (ClientMinioImplemantationException e) {
            }
        }
        return usr;
    }


    public UserRecord save(UserRecord dados) throws ClientMinioImplemantationException {
        var entity = repository.getReferenceById(dados.id());
        entity.setUser(dados);
        try {
            boolean did = clienteMinio.updateObject(entity);
            entity = repository.save(entity);
            if (did) clienteMinio.insertSasUrl(entity);
            return new UserRecord(entity);
        } catch (ClientMinioImplemantationException e) {
            throw new ClientMinioImplemantationException(e);
        }
    }

    public UserRecord delete(Long id){
        var entity = repository.getReferenceById(id);
        entity.setAtivo(false);
        repository.save(entity);
        return new UserRecord(entity);
    }

    public UserRecord deleteForever(Long id){
        var entity = repository.getReferenceById(id);
        try {
            clienteMinio.remove(entity);
            repository.delete(entity);
        } catch (ClientMinioImplemantationException e) {
            throw new RuntimeException(e);
        }
        return new UserRecord(new User());
    }

    public List<UserRecord> searchUsersByName(String search_name){

        return searchUserByNomeCompleto(search_name);
    }

    private UserRecord getUserRecord(User user){
        if (user != null) {
            try {
                clienteMinio.insertSasUrl(user);
            } catch (ClientMinioImplemantationException e) {
                e.printStackTrace();
            }
        }
        return new UserRecord(user == null ? new User() : user);
    }
    public UserRecord getUserById(Long id){
        return getUserRecord(repository.getReferenceById(id));
    }

    public UserRecord getUserByCPF(String cpf){
        return getUserRecord(repository.findByCpf(cpf));
    }
    private List<UserRecord> searchUserByNomeCompleto(String search_name){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserRecord> cq = cb.createQuery(UserRecord.class);
        Root<User> root = cq.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        String[] tokens = search_name.trim().toLowerCase(Locale.ROOT).split("\\s+");
        Path<String> p = root.get("nomeCompleto");

        for(String v : tokens) {
            predicates.add(cb.like(cb.function("unaccent",String.class, cb.lower(p)), "%" + StringUtils.stripAccents(v.toLowerCase()) + "%"));
        }
        cq.multiselect(
            root.get("id"),
            root.get("cpf"),
            root.get("ativo"),
            root.get("posto"),
            root.get("quadro"),
            root.get("especialidade"),
            root.get("nomeGuerra"),
            root.get("nomeCompleto"))
                .where(cb.and(predicates.toArray(new Predicate[0])));

            return entityManager.createQuery(cq).getResultList();
    }

}
