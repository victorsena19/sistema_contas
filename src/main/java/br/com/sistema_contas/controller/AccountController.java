package br.com.sistema_contas.controller;

import br.com.sistema_contas.dto.AccountDTO;
import br.com.sistema_contas.dto.AccountSummaryDTO;
import br.com.sistema_contas.dto.AccountTypeDTO;
import br.com.sistema_contas.enums.AccountType;
import br.com.sistema_contas.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/all")
    public ResponseEntity<AccountSummaryDTO> getAllAccountsByUser(){
        return ResponseEntity.ok(accountService.getAllAccountsByUser());
    }

    @GetMapping("/pending")
    public ResponseEntity<AccountSummaryDTO> getAccountByUserStatusPending(){
        return ResponseEntity.ok(accountService.getAccountByUserStatusPending());
    }

    @GetMapping
    public ResponseEntity<AccountTypeDTO> getAccountByUserAndAccountType(@RequestParam AccountType type){
        return ResponseEntity.ok(accountService.getAccountByUserAndAccountType(type));
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO){
        AccountDTO savedAccount = accountService.createAccount(accountDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id,
                                                    @RequestBody AccountDTO accountDTO) {
        AccountDTO updatedAccount = accountService.updateAccount(id, accountDTO);
        return ResponseEntity.ok().body(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

}
