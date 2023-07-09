package mil.decea.mentorpgapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import mil.decea.mentorpgapi.domain.daoservices.DocumentsService;
import mil.decea.mentorpgapi.domain.daoservices.UserService;
import mil.decea.mentorpgapi.domain.daoservices.minio.ClientMinioImplemantationException;
import mil.decea.mentorpgapi.domain.user.AuthUserRecord;
import mil.decea.mentorpgapi.domain.user.UserRecord_old;
import mil.decea.mentorpgapi.domain.user.validation.annotations.IsValidCpf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Validated
@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    private final UserService userService;
    private final DocumentsService documentsService;

    @Autowired
    public UserController(UserService userService, DocumentsService documentsService) {
        this.userService = userService;
        this.documentsService = documentsService;
    }

    @PostMapping("/save")
    @Transactional
    public ResponseEntity save(@RequestBody @Valid UserRecord_old dados) throws ClientMinioImplemantationException{
        return ResponseEntity.ok(userService.save(dados));
    }

    @PostMapping("/otherpsw")
    @Secured({"ADMIN","COORDENADOR"})
    @Transactional
    public ResponseEntity changeOthersPassword(@RequestBody @Valid AuthUserRecord dados) {

        if (Objects.equals(dados.id(),dados.dataAuthorityRecord().id())) {
            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Operação não autorizada!");
        }
        userService.changePassword(dados);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/ownpsw")
    @Transactional
    public ResponseEntity changeOwnPassword(@RequestBody @Valid AuthUserRecord dados) {
        //System.out.println("Alterando senha de " + dados.cpf());
        userService.changePassword(dados);
        return ResponseEntity.ok().build();
    }

    /*
       Por padrão esse recurso vem desabilitado no spring Security, sendo que para o utilizar devemos adicionar
       a seguinte anotação na classe Securityconfigurations do projeto:
       @EnableMethodSecurity(securedEnabled = true)
    */
    @DeleteMapping("/{id}")
    @Transactional
    @Secured({"ADMIN","COORDENADOR"})
    public ResponseEntity delete(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.delete(id));
    }

    @DeleteMapping("/forever/{id}")
    @Transactional
    @Secured({"ADMIN"})
    public ResponseEntity deleteForever(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.deleteForever(id));
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
    public ResponseEntity userSearch(@PathVariable("search_name") @Size(min = 3) String search_name){
        return ResponseEntity.ok(userService.searchUsersByName(search_name));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity getUserByID(@PathVariable("id") Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }



    @GetMapping("/cpf/{cpf}")
    @Transactional
    public ResponseEntity getUserByCPF(@PathVariable("cpf") @IsValidCpf String cpf){
        return ResponseEntity.ok(userService.getUserByCPF(cpf.replaceAll("\\D","")));
    }

    @GetMapping("/error/{code}")
    @Transactional
    public ResponseEntity testErrorCode(@PathVariable Integer code){
        return ResponseEntity.status(code).build();
    }

    @GetMapping("/personaldocstypes")
    @Transactional
    public ResponseEntity getPersonalDocsTypes(){
        return ResponseEntity.ok(documentsService.getAllPersonalDocumentsTypesActives());
    }


}
