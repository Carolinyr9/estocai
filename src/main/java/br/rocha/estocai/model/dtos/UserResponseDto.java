package br.rocha.estocai.model.dtos;

import br.rocha.estocai.model.enums.UserRole;

public record UserResponseDto(Long id, String username, String email, UserRole role) {
    
}
