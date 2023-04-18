package mil.decea.mentorpgapi.controller;

import jakarta.validation.Valid;
import mil.decea.mentorpgapi.apisupport.security.DataTokenJWT;
import mil.decea.mentorpgapi.apisupport.security.TokenService;
import mil.decea.mentorpgapi.domain.user.LoginRecord;
import mil.decea.mentorpgapi.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class Authentication {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid LoginRecord dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.cpf(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
    }


}
