package br.com.sistema_contas.mapper;

import br.com.sistema_contas.dto.AccountByUserDTO;
import br.com.sistema_contas.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountByUserMapper {

    AccountByUserDTO toDTO(Account account);

}
