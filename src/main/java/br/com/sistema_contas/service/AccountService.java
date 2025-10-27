package br.com.sistema_contas.service;

import br.com.sistema_contas.dto.AccountTypeDTO;
import br.com.sistema_contas.enums.Status;
import br.com.sistema_contas.global_exception.CustomAccessDeniedException;
import br.com.sistema_contas.util.Util;
import br.com.sistema_contas.dto.AccountByUserDTO;
import br.com.sistema_contas.dto.AccountDTO;
import br.com.sistema_contas.dto.AccountSummaryDTO;
import br.com.sistema_contas.enums.AccountType;
import br.com.sistema_contas.mapper.AccountByUserMapper;
import br.com.sistema_contas.mapper.AccountMapper;
import br.com.sistema_contas.model.Account;
import br.com.sistema_contas.model.User;
import br.com.sistema_contas.repostory.AccountRepository;
import br.com.sistema_contas.repostory.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final AccountByUserMapper accountByUserMapper;
    private final UserRepository userRepository;
    private final Util util;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper,
                          AccountByUserMapper accountByUserMapper,
                          UserRepository userRepository,
                          Util util) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.accountByUserMapper = accountByUserMapper;
        this.userRepository = userRepository;
        this.util = util;
    }

    /**Pega todas as contas pendentes do Usuario logado
     * soma o montante a pagar e a receber
     * retorna o montante a receber e montante a pagar
     * retorna a diferença do montante a receber e a pagar
    */
     public AccountSummaryDTO getAccountByUserStatusPending() {
        User userLogged = util.getUserLogged();
        User user = userRepository.findById(userLogged.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        List<AccountByUserDTO> accountByUserDTO = accountRepository
                .findByUserIdAndStatus(user.getId(), Status.PENDING)
                .stream()
                .map(accountByUserMapper::toDTO)
                .collect(Collectors.toList());

        BigDecimal totalAmountPay = BigDecimal.ZERO;
        BigDecimal totalAmountReceive = BigDecimal.ZERO;

        for (AccountByUserDTO accountByUser : accountByUserDTO) {
            if (accountByUser.getAccountType().equals(AccountType.PAY) && accountByUser.getStatus().equals(Status.PENDING)) {
                totalAmountPay = totalAmountPay.add(accountByUser.getAmount());
            } else if (accountByUser.getAccountType().equals(AccountType.RECEIVE) && accountByUser.getStatus().equals(Status.PENDING)) {
                totalAmountReceive = totalAmountReceive.add(accountByUser.getAmount());
            }
        }

        BigDecimal differenceAmount = totalAmountReceive.subtract(totalAmountPay);

        return new AccountSummaryDTO(accountByUserDTO, totalAmountPay, totalAmountReceive, differenceAmount);
    }

    /**Pega todas as contas do Usuario logado
     * soma o montante a pagar e a receber
     * retorna o montante a receber e montante a pagar
     * retorna a diferença do montante a receber e a pagar
     */
    public AccountSummaryDTO getAllAccountsByUser() {
        User userLogged = util.getUserLogged();
        User user = userRepository.findById(userLogged.getId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        List<AccountByUserDTO> accountByUserDTO = accountRepository
                .findByUserId(user.getId())
                .stream()
                .map(accountByUserMapper::toDTO)
                .collect(Collectors.toList());

        BigDecimal totalAmountPay = BigDecimal.ZERO;
        BigDecimal totalAmountReceive = BigDecimal.ZERO;

        for (AccountByUserDTO accountByUser : accountByUserDTO) {
            if (accountByUser.getAccountType().equals(AccountType.PAY)) {
                totalAmountPay = totalAmountPay.add(accountByUser.getAmount());
            } else if (accountByUser.getAccountType().equals(AccountType.RECEIVE)) {
                totalAmountReceive = totalAmountReceive.add(accountByUser.getAmount());
            }
        }

        BigDecimal differenceAmount = totalAmountReceive.subtract(totalAmountPay);

        return new AccountSummaryDTO(accountByUserDTO, totalAmountPay, totalAmountReceive, differenceAmount);
    }

    /**Pega todas as contas do Usuario logado pelo Tipo da conta PAGAR OU RECEBER
     * soma o montante a pagar e a receber
     * retorna o montante a receber e montante a pagar
     * retorna a diferença do montante a receber e a pagar
     */
    public AccountTypeDTO getAccountByUserAndAccountType(AccountType accountType){
        User userLogged = util.getUserLogged();
         List<Account> account = accountRepository
                .findByUserIdAndAccountType(userLogged.getId(), accountType);
         List<AccountByUserDTO> listAccountTypeDTO = account.stream().map(accountByUserMapper::toDTO).toList();
         BigDecimal totalAmount = BigDecimal.ZERO;

         for (AccountByUserDTO accountByUserDTO : listAccountTypeDTO){
             totalAmount = totalAmount.add(accountByUserDTO.getAmount());
         }
         return new AccountTypeDTO(listAccountTypeDTO, totalAmount);
    }

    /**
     * Registra uma nova Conta do Usuario logado
     */
    public AccountDTO createAccount(AccountDTO accountDTO){
       Account account = accountMapper.toEntity(accountDTO);
        User userLogged = util.getUserLogged();
       User user =  userRepository.findById(userLogged.getId())
                .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));
        account.setUser(user);
        Account savedAccount = accountRepository.save(account);
        return accountMapper.toDTO(savedAccount);
    }

    /**
     * Atualiza uma Conta existente do Usuario logado
     */
    public AccountDTO updateAccount(Long id, AccountDTO accountDTO) {
        Account accountExists = accountRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Conta não encontrada"));
        User userLogged = util.getUserLogged();
        User user = userRepository.findById(userLogged.getId())
                .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));

        if (!accountExists.getUser().getId().equals(user.getId())){
            throw new CustomAccessDeniedException("Você não tem permissão para atualizar essa conta");
        }
        accountExists.setUser(user);
        accountMapper.updateFromDTO(accountDTO, accountExists);
        Account updatedAccount = accountRepository.save(accountExists);
        return accountMapper.toDTO(updatedAccount);
    }

    /**
     * Deleta uma Conta do Usuario logado
     */
    public void deleteAccount(Long id){
        User user = util.getUserLogged();
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new CustomAccessDeniedException("Você não tem permissão para excluir essa conta.");
        }

        accountRepository.deleteById(id);
    }
}
