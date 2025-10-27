package br.com.sistema_contas.model;

import br.com.sistema_contas.enums.AccountType;
import br.com.sistema_contas.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    private String description;
    private Double amount;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate expirationDate;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(cascade = CascadeType.MERGE)
    private User user;

}
