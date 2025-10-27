package br.com.sistema_contas.repostory;

import br.com.sistema_contas.enums.AccountType;
import br.com.sistema_contas.enums.Status;
import br.com.sistema_contas.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long id);
    List<Account> findByUserIdAndAccountType(Long id, AccountType accountType);
    List<Account> findByUserIdAndStatus(Long id, Status status);
}
