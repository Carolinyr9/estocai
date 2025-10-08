package br.rocha.estocai.model.dtos;

import java.util.Optional;

public record CategoryPatchDto(Optional<String> name, Optional<String> description) {
} 