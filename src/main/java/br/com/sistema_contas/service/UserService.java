package br.com.sistema_contas.service;

import br.com.sistema_contas.dto.UserDTO;
import br.com.sistema_contas.global_exception.CustomAccessDeniedException;
import br.com.sistema_contas.mapper.UserMapper;
import br.com.sistema_contas.model.Account;
import br.com.sistema_contas.model.User;
import br.com.sistema_contas.repostory.AccountRepository;
import br.com.sistema_contas.repostory.UserRepository;
import br.com.sistema_contas.util.Util;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountRepository accountRepository;
    private final Util util;


    @Autowired
    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       AccountRepository accountRepository, Util util) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.accountRepository = accountRepository;
        this.util = util;
    }
    /**
     *  listar Usuario por email
     */
    public UserDetails listUserByEmail(String email){
        UserDetails user = userRepository.findByEmail(email);
        if (user == null){
            throw new EntityNotFoundException("Usuario não encontrado");
        }
        return user;
    }

    /**
     * Registra um novo Usuario
     */
    public UserDTO createUser(UserDTO userDTO){
        User user = userMapper.toEntity(userDTO);
        boolean userExists = userRepository.existsByEmailIgnoreCase(userDTO.getEmail());
        if (userExists){
            throw new IllegalArgumentException("Usuario já cadastrado");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    /**
     * Atualiza o Usuario, mas somente o proprio usuario pode atualizar
     */
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User userExists = userRepository
                .findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));
        User userLogged = util.getUserLogged();
        if (!userExists.getId().equals(userLogged.getId())){
            throw new CustomAccessDeniedException("Você não tem permissão para atualizar esse usuário");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(userExists.getPassword());
        userExists.setPassword(encryptedPassword);
        userMapper.updateFromDTO(userDTO, userExists);
        User updatedUser = userRepository.save(userExists);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Deleta o Usuario logado
     */
    public void deleteUser(Long id){
        User userLogged = util.getUserLogged();
        userRepository
                .findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Usuario não encontrado"));
        List<Account> listAccount = accountRepository.findByUserId(id);

        if (!userLogged.getId().equals(id)){
            throw new CustomAccessDeniedException("Você não pode apagar um usuário que não seja o seu");
        };

        if (!listAccount.isEmpty()){
            throw new IllegalArgumentException("Não é possível apagar um usuário com contas lançadas");
        }
        userRepository.deleteById(id);
    }
}
