package br.rocha.estocai.model.dtos;

import java.util.Optional;

public record UserPatchDto(Optional<String> username, Optional<String> email, Optional<String> password) {
    
}
