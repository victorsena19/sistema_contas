package br.com.sistema_contas.controller;

import br.com.sistema_contas.dto.AutenticacaoDTO;
import br.com.sistema_contas.dto.ResponseLoginDTO;
import br.com.sistema_contas.dto.ResponseUserDTO;
import br.com.sistema_contas.model.User;
import br.com.sistema_contas.service.TokenService;
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
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Autowired
    public LoginController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody AutenticacaoDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateTokenJwt((User) auth.getPrincipal());

        User userLogado = (User) auth.getPrincipal();
        ResponseUserDTO usuario = new ResponseUserDTO(
                userLogado.getId(),
                userLogado.getName(),
                userLogado.getEmail()
        );

        return ResponseEntity.ok(new ResponseLoginDTO(token, usuario));
    }
}
