package br.rocha.estocai.repository;

import br.rocha.estocai.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    public Category findByName(String name);
}
