package br.com.sistema_contas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountSummaryDTO {
    private List<AccountByUserDTO> accounts;
    private BigDecimal totalAmountPay;
    private BigDecimal totalAmountReceive;
    private BigDecimal differenceAmount;
}