package br.com.sistema_contas.mapper;

import br.com.sistema_contas.dto.UserDTO;
import br.com.sistema_contas.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDTO user);

    UserDTO toDTO(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(UserDTO userDTO, @MappingTarget User user);
}
