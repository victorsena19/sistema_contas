package br.com.sistema_contas.security;

import br.com.sistema_contas.global_exception.StandardError;
import br.com.sistema_contas.service.UserService;
import br.com.sistema_contas.service.TokenService;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UserService userService;

    private final ObjectMapper mapper;

    @Autowired
    public SecurityFilter(
            TokenService tokenService,
            UserService userService,
            ObjectMapper mapper) {
        this.tokenService = tokenService;
        this.userService = userService;
        this.mapper = mapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        var token = this.getToken(request);
        try{
            if (token != null){
                var email = tokenService.validateTokenJwt(token);
                UserDetails user = userService.listUserByEmail(email);

                var authentication = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        }catch (TokenExpiredException ex){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType("application/json");
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            response.getWriter().write(
                    mapper.writeValueAsString(
                            new StandardError(
                                    LocalDateTime.now(),
                                    HttpStatus.FORBIDDEN.value(),
                                    "Token expirado. Faça login novamente.",
                                    request.getRequestURI()
                            )
                    ));
        }
    }

    private String getToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
