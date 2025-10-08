package br.rocha.estocai.model.dtos;

import java.util.List;

import br.rocha.estocai.model.Product;

public record CategoryResponseDto(Long id, String name, String description, List<Product> products) {
    
}
