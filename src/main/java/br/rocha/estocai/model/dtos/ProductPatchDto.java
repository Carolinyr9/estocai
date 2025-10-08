package br.rocha.estocai.model.dtos;

import java.util.Optional;

public record ProductPatchDto(Optional<String> name, Optional<String> description, Optional<Double> price, Optional<Integer> quantity, Optional<Long> categoryId) {
    
}
