package br.com.sistema_contas.dto;

import br.com.sistema_contas.enums.AccountType;
import br.com.sistema_contas.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO{
        private Long id;
        private AccountType accountType;
        private String description;
        private BigDecimal amount;
        @JsonFormat(pattern = "dd/MM/yyyy")
        private LocalDate expirationDate;
        private Status status;
        private UserDTO user;
}
