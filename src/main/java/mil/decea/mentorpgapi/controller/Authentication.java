package mil.decea.mentorpgapi.controller;

import jakarta.validation.Valid;
import mil.decea.mentorpgapi.domain.user.LoginRecord;
import mil.decea.mentorpgapi.domain.user.User;
import mil.decea.mentorpgapi.etc.security.TokenService;
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

    public Authentication(@Autowired AuthenticationManager manager, @Autowired TokenService tokenService) {
        this.manager = manager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody @Valid LoginRecord dados) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.cpf(), dados.senha());

        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(tokenJWT);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN","ROLE_USER"})
    public ResponseEntity<?> checarAutenticado(){
        return ResponseEntity.ok("");
    }

}
