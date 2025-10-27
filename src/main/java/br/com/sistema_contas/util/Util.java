package br.com.sistema_contas.util;

import br.com.sistema_contas.model.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class Util {

    /**
     * Pega o Usuario logado
     */
    public User getUserLogged(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User)){
            throw new BadCredentialsException("Usuário não autenticado");
        }
        return (User) auth.getPrincipal();
    }
}
