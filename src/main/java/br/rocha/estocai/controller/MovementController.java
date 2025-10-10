package br.rocha.estocai.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.rocha.estocai.model.dtos.MovementResponseDto;
import br.rocha.estocai.service.MovementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/movements")
public class MovementController {
    @Autowired
    MovementService movementService;

    @GetMapping
    public ResponseEntity<Page<MovementResponseDto>> getAllMovements(Pageable pageable){
        Page<MovementResponseDto> movement = movementService.getAllMovements(pageable);
        return ResponseEntity.ok(movement);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByType(Pageable pageable, @PathVariable String type){
        Page<MovementResponseDto> movement = movementService.getMovementsByType(type, pageable);
        return ResponseEntity.ok(movement);
    }

    @GetMapping("/description/{description}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByDescription(Pageable pageable, @PathVariable String description){
        Page<MovementResponseDto> movement = movementService.getMovementsByDescription(description, pageable);
        return ResponseEntity.ok(movement);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByDProductId(Pageable pageable, @PathVariable Long productId){
        Page<MovementResponseDto> movement = movementService.getMovementsByProductId(productId, pageable);
        return ResponseEntity.ok(movement);
    }

    @GetMapping("/date/{startDate}/{endDate}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByDate(Pageable pageable, @PathVariable Date startDate, @PathVariable Date endDate){
        Page<MovementResponseDto> movement = movementService.getMovementsByDate(startDate, endDate, pageable);
        return ResponseEntity.ok(movement);
    }
    
}
