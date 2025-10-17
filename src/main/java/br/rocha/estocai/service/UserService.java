package br.rocha.estocai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.rocha.estocai.model.dtos.UserResponseDto;
import br.rocha.estocai.model.enums.UserRole;
import br.rocha.estocai.exceptions.ResourceNotFoundException;
import br.rocha.estocai.mappers.UserMapper;
import br.rocha.estocai.model.dtos.UserPatchDto;
import br.rocha.estocai.model.User;
import br.rocha.estocai.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Transactional
    public UserResponseDto updateUserPartial(Long id, UserPatchDto data){
        User existingUser = findExistingUser(id);
        
        data.username().ifPresent(existingUser::setUsername);
        
        data.email().ifPresent(existingUser::setEmail);

        data.password().ifPresent(newPassword -> {
            String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
            existingUser.setPassword(encodedPassword);
        });

        User userSaved = userRepository.save(existingUser);
        return userMapper.userToUserResponseDto(userSaved);
    }

    public void deleteUser(Long id){
        User existingUser = findExistingUser(id);

        userRepository.delete(existingUser);
    }

    @Transactional
    public UserResponseDto updateRole(Long id, UserRole role){
        User existingUser = findExistingUser(id);
        existingUser.setRole(role);

        User userSaved = userRepository.save(existingUser);
        return userMapper.userToUserResponseDto(userSaved);
    }

    private User findExistingUser(Long id){
        return userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found " + id));
    }
}
