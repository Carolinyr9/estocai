package br.rocha.estocai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.rocha.estocai.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
