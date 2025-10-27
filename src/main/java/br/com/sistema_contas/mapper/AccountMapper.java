package br.com.sistema_contas.mapper;

import br.com.sistema_contas.dto.AccountDTO;
import br.com.sistema_contas.model.Account;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface AccountMapper {

    Account toEntity(AccountDTO accountDTO);

    AccountDTO toDTO(Account account);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(AccountDTO accountDTO, @MappingTarget Account account);
}
