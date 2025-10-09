package br.rocha.estocai.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;
import br.rocha.estocai.repository.MovementRepository;

public class MovementService {
    @Autowired
    MovementRepository movementRepository;

    public void createProduct(Product product){
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setDate(new Date());
        movement.setType(MovementType.ENTRY);
        movement.setDescription(MovementDescription.ADDED);
    }

    public void removeProduct(Product product){
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setDate(new Date());
        movement.setType(MovementType.EXIT);
        movement.setDescription(MovementDescription.REMOVED);
    }

    public void decreaseQuantity(Product product){
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setDate(new Date());
        movement.setType(MovementType.EXIT);
        movement.setDescription(MovementDescription.QUANTITY_DECREASED);
    }

    public void increaseQuantity(Product product){
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setDate(new Date());
        movement.setType(MovementType.ENTRY);
        movement.setDescription(MovementDescription.QUANTITY_INCREASED);
    }

    public void updateProduct(Product product){
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setDate(new Date());
        movement.setType(MovementType.EDITED);
        movement.setDescription(MovementDescription.EDITED);
    }
}
