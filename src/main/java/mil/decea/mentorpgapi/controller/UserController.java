package mil.decea.mentorpgapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import mil.decea.mentorpgapi.domain.daoservices.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    @Transactional
    public ResponseEntity save(@RequestBody @Valid UserRecord dados) throws ClientMinioImplemantationException{
        /*try {
            return ResponseEntity.ok(userService.save(dados));
        } catch (ClientMinioImplemantationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: falha ao atualizar foto");
        }*/
        return ResponseEntity.ok(userService.save(dados));
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
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/forever/{id}")
    @Transactional
    @Secured({"ADMIN"})
    public ResponseEntity deleteForever(@PathVariable Long id){
        userService.deleteForever(id);
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
        return ResponseEntity.ok(userService.searchUsersByName(search_name));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity getUserByID(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }


    @GetMapping("/cpf/{cpf}")
    @Transactional
    public ResponseEntity getUserByCPF(@PathVariable String cpf){
        return ResponseEntity.ok(userService.getUserByCPF(cpf.replaceAll("\\D","")));
    }

    @GetMapping("/error/{code}")
    @Transactional
    public ResponseEntity testErrorCode(@PathVariable Integer code){
        return ResponseEntity.status(code).build();
    }

}
