package br.com.sistema_contas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeDTO {
    List<AccountByUserDTO> accounts;
    private BigDecimal totalAmount;
}
