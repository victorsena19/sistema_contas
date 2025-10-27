package br.com.sistema_contas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLoginDTO {
    private String token;
    private ResponseUserDTO usuario;
}
