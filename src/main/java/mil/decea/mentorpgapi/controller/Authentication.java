package mil.decea.mentorpgapi.controller;

import jakarta.validation.Valid;
import mil.decea.mentorpgapi.domain.daoservices.UserService;
import mil.decea.mentorpgapi.domain.user.LoginRecord;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.domain.user.UserRecord;
import mil.decea.mentorpgapi.etc.security.FirstAdminRecord;
import mil.decea.mentorpgapi.etc.security.TokenService;
import mil.decea.mentorpgapi.etc.security.UserJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class Authentication {

    private final AuthenticationManager manager;

    private final TokenService tokenService;

    private final UserService userService;
    @Autowired
    public Authentication( AuthenticationManager manager, TokenService tokenService, UserService userService) {
        this.manager = manager;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid LoginRecord dados) {
        return ResponseEntity.ok(authenticate(dados.cpf(), dados.senha()));
    }


    @PostMapping("/firstAdmin")
    public ResponseEntity<?> criarPrimeiroAdmin(@RequestBody @Valid FirstAdminRecord dados) {
        UserRecord _usr = userService.createFirstAdmin(dados);
        return ResponseEntity.ok(authenticate(_usr.cpf(), dados.senha()));
    }

    private UserJWT authenticate(String cpf, String senha){
        var authenticationToken = new UsernamePasswordAuthenticationToken(cpf, senha);
        var authentication = manager.authenticate(authenticationToken);
        return tokenService.gerarToken((User) authentication.getPrincipal());
    }

    @GetMapping
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<?> checarAutenticado(){
        return ResponseEntity.ok("");
    }

}
