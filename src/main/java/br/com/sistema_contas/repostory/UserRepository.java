package br.com.sistema_contas.repostory;

import br.com.sistema_contas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    UserDetails findByEmail(String email);

    boolean existsByEmailIgnoreCase(String email);
}
