package br.rocha.estocai.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.rocha.estocai.mappers.MovementMapper;
import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.dtos.MovementResponseDto;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;
import br.rocha.estocai.repository.MovementRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovementService {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private MovementMapper mapper;

    @Transactional
    public void createProduct(Product product) {
        registerMovement(product, MovementType.ENTRY, MovementDescription.ADDED);
    }

    @Transactional
    public void removeProduct(Product product) {
        registerMovement(product, MovementType.EXIT, MovementDescription.REMOVED);
    }

    @Transactional
    public void decreaseQuantity(Product product) {
        registerMovement(product, MovementType.EXIT, MovementDescription.QUANTITY_DECREASED);
    }

    @Transactional
    public void increaseQuantity(Product product) {
        registerMovement(product, MovementType.ENTRY, MovementDescription.QUANTITY_INCREASED);
    }

    @Transactional
    public void updateProduct(Product product) {
        registerMovement(product, MovementType.EDITED, MovementDescription.EDITED);
    }

    @Transactional
    public void consultProduct(Product product) {
        registerMovement(product, MovementType.NONE, MovementDescription.CONSULT);
    }

    @Transactional(readOnly = true)
    public Page<MovementResponseDto> getAllMovements(Pageable pageable){
        return movementRepository.findAll(pageable).map(mapper::movementToMovementResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<MovementResponseDto> getMovementsByProductId(Long productId, Pageable pageable){
        return movementRepository.findByProductId(productId, pageable).map(mapper::movementToMovementResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<MovementResponseDto> getMovementsByType(String type, Pageable pageable){
        return movementRepository.findByType(MovementType.valueOf(type.toUpperCase()), pageable).map(mapper::movementToMovementResponseDto);
    }

    @Transactional(readOnly = true)
    public Page<MovementResponseDto> getMovementsByDescription(String description, Pageable pageable){
        return movementRepository.findByDescription(MovementDescription.valueOf(description.toUpperCase()), pageable).map(mapper::movementToMovementResponseDto);
    }

    private void registerMovement(Product product, MovementType type, MovementDescription description) {
        Movement movement = new Movement();
        movement.setProduct(product);
        movement.setDate(new Date());
        movement.setType(type);
        movement.setDescription(description);
        movementRepository.save(movement);
    }

}
