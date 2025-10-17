package br.rocha.estocai.mappers;

import org.mapstruct.Mapper;

import br.rocha.estocai.model.User;
import br.rocha.estocai.model.dtos.UserPatchDto;
import br.rocha.estocai.model.dtos.UserResponseDto;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToUserResponseDto(User user); 

}
