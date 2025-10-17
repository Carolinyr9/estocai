package br.rocha.estocai.model.dtos;

import br.rocha.estocai.model.enums.UserRole;

public record RegisterDto(String username, String email, String password, UserRole role) {
    
}
