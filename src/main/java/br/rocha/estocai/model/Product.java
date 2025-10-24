package br.rocha.estocai.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micrometer.common.lang.NonNull;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @Column(unique=true)
    private String name;

    @NonNull
    private String description;

    @NonNull
    @Positive
    private Double price;

    @NonNull
    @Positive
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<Movement> movements;

    public Product(String name, String description, Double price, Integer quantity){
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(String name, String description, Double price, Integer quantity, Category category){
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

}
