package mil.decea.mentorpgapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import mil.decea.mentorpgapi.domain.daoservices.UserRepository;
import mil.decea.mentorpgapi.domain.user.Posto;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity save(@RequestBody @Valid UserRecord dados){
        var entity = repository.getReferenceById(dados.id());
        entity.updateValues(dados);
        entity = repository.save(entity);
        return ResponseEntity.ok(new UserRecord(entity));
    }


    /*
       Por padrão esse recurso vem desabilitado no spring Security, sendo que para o utilizar devemos adicionar
       a seguinte anotação na classe Securityconfigurations do projeto:
       @EnableMethodSecurity(securedEnabled = true)
    */
    @DeleteMapping("/{id}")
    @Transactional
    @Secured({"ADMIN","COORDENADOR"})
    public ResponseEntity delete(@PathVariable Long id){
        var entity = repository.getReferenceById(id);
        entity.setAtivo(false);
        repository.save(entity);
        return ResponseEntity.noContent().build();
    }
/*

    @GetMapping
    public ResponseEntity<Page<UserRecord>> userList(Pageable pageable){
        //o spring consegue montar a clausula where baseado no tributo e valor anexado no nome do método findAllBy
        var page = repository.findAllByAtivoTrue(pageable).stream().map(UserRecord::new);
        return ResponseEntity.ok(page);
    }
*/
    @GetMapping("/search/{search_name}")
    @Transactional
    public ResponseEntity userSearch(@PathVariable String search_name){
        var users = repository.findAll(UserRepository.searchUserByNomeCompleto(search_name));
        return ResponseEntity.ok(users.stream().map(UserRecord::new));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity getUserByID(@PathVariable Long id){
        var user = repository.getReferenceById(id);
        return ResponseEntity.ok(new UserRecord(user));
    }

    @GetMapping("/error/{code}")
    @Transactional
    public ResponseEntity getUserByID(@PathVariable Integer code){
        return ResponseEntity.status(code).build();
    }

}
