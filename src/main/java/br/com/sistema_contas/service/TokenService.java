package br.com.sistema_contas.service;

import br.com.sistema_contas.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um novo Token
     */
    public String generateTokenJwt(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            //Cria um token jwt e retorna-o
            return JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token jwt" + e);
        }
    }

    /**
     * Valida o Token
     */
    public String validateTokenJwt(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTCreationException e) {
            throw new IllegalArgumentException("Token inválido ou expirado" + e);
        }
    }

    /**
     * Gera o tempo de expiração do token
     */
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(5).toInstant(ZoneOffset.of("-04:00"));
    }
}
