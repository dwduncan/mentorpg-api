package mil.decea.mentorpgapi.domain.daoservices;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import mil.decea.mentorpgapi.domain.changewatch.ObjectChangesChecker;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.FieldChangedWatcher;
import mil.decea.mentorpgapi.domain.changewatch.logs.ObjecCreatedLog;
import mil.decea.mentorpgapi.domain.changewatch.logs.ObjecRemovedLog;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClienteMinio;
import mil.decea.mentorpgapi.domain.daoservices.repositories.UserRepository;
import mil.decea.mentorpgapi.domain.user.*;
import mil.decea.mentorpgapi.etc.exceptions.MentorValidationException;
import mil.decea.mentorpgapi.etc.security.FirstAdminRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    ChangeLogService changeLogService;
    private final ClienteMinio clienteMinio;
    //private final List<DTOValidator<UserRecord>> validators;
    private final EntityManager entityManager;

    @Autowired
    public UserService( UserRepository repository,
                        ChangeLogService changeLogService,
                        //List<DTOValidator<UserRecord>> validators,
                        EntityManager entityManager,
                        ClienteMinio clienteMinio) {
        this.repository = repository;
        this.changeLogService = changeLogService;
        //this.validators = validators;
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
                        root.get("nomeCompleto"),
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
            } catch (ClientMinioImplemantationException ignore) {}
        }
        return usr;
    }

    public void changePassword(AuthUserRecord authUser) {

        if (authUser.senha() != null && authUser.senha().trim().length() > 7){

            var entity = repository.getReferenceById(authUser.id());

            if (authUser.senhaAntiga() != null && !authUser.senhaAntiga().isBlank()){
                if (!DefaultPasswordEncoder.matchesPasswords(authUser.senhaAntiga().trim(),entity.getPassword())){
                    throw new MentorValidationException("Senha atual não confere");
                }
            }

            String _encryptPassword = DefaultPasswordEncoder.encode(authUser.senha());
            entity.setSenha(_encryptPassword);
            entity = repository.save(entity);

            //AuthUser ausr = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            FieldChangedLog log = new FieldChangedLog("senha", entity, entity);
            if (log.isChanged()) changeLogService.insert(log);

        }else{
            throw new MentorValidationException("A senha atual deve possuir no mínimo 8 caractéres");
        }
    }

    @Transactional
    public UserRecord save(UserRecord dados) throws ClientMinioImplemantationException {

        AuthUser ausr = (AuthUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean newEntity = dados.id() == null;

        var entity = newEntity ? new User() : repository.getReferenceById(dados.id());

        ObjectChangesChecker changes = entity.onValuesUpdated(dados);

        try {

            boolean did = clienteMinio.updateObject(entity);

            entity = repository.save(entity);

            if (did) clienteMinio.insertSasUrl(entity);

            if (newEntity) changes.setObjectAndParentIdIfEquals(entity.getId());

            changeLogService.insert(changes);//, ausr

            return new UserRecord(entity);
        } catch (ClientMinioImplemantationException e) {
            throw new ClientMinioImplemantationException(e);
        } catch (Exception e){
            e.printStackTrace();
            throw new MentorValidationException("Erro no servidor ao tentar salvar");
        }
    }


    public UserRecord delete(Long id){
        var entity = repository.getReferenceById(id);
        entity.setAtivo(false);
        repository.save(entity);
        ObjecRemovedLog log = new ObjecRemovedLog(entity,null,false);
        changeLogService.insert(log);
        return new UserRecord(entity);
    }

    public UserRecord deleteForever(Long id){
        var entity = repository.getReferenceById(id);
        try {

            clienteMinio.remove(entity);
            repository.delete(entity);
            ObjecRemovedLog log = new ObjecRemovedLog(entity,null,true);
            changeLogService.insert(log);

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

    public UserRecord createFirstAdmin(FirstAdminRecord usr){

        if (numberOfUsers() != null && numberOfUsers().intValue() != 0) throw new AccessDeniedException("Acesso negado, já existem usuários cadastrados");

        String _encryptPassword = DefaultPasswordEncoder.encode(usr.senha());

        User _user = new User();
        _user.setCpf(usr.cpf());
        _user.setSenha(_encryptPassword);
        _user.setNomeGuerra(usr.nomeGuerra());
        _user.setNomeCompleto(usr.nomeCompleto());
        _user.setCelular(usr.celular());
        _user.setEmail(usr.email());
        _user.setRole(Roles.ADMIN.getRoleName());

        _user = repository.save(_user);

        return new UserRecord(_user);
    }

    public Long numberOfUsers(){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<User> root = cq.from(User.class);
        cq.select(cb.count(root.get("id")));
        return entityManager.createQuery(cq).getSingleResult();
    }

}
