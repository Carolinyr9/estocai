package br.rocha.estocai.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "movements")
public class Movement {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;
    
    @Nonnull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private MovementType type;

    @Nonnull
    @Enumerated(EnumType.STRING)
    private MovementDescription description;

    //private User user

}
