package br.rocha.estocai.controller;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.rocha.estocai.model.dtos.MovementResponseDto;
import br.rocha.estocai.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/movements")
public class MovementController {
    @Autowired
    MovementService movementService;

    @Operation(
        summary = "Get movements",
        description = "Return a pageable with all movements registred in system",
        responses = {
            @ApiResponse(responseCode = "200", description = "Movements found"),
            @ApiResponse(responseCode = "404", description = "Movements not found")
        }
    )
    @GetMapping
    public ResponseEntity<Page<MovementResponseDto>> getAllMovements(Pageable pageable){
        Page<MovementResponseDto> movement = movementService.getAllMovements(pageable);
        return ResponseEntity.ok(movement);
    }

    @Operation(
        summary = "Get movements by type",
        description = "Return a pageable with all movements registred with the type",
        responses = {
            @ApiResponse(responseCode = "200", description = "Movements found"),
            @ApiResponse(responseCode = "404", description = "Movements not found")
        }
    )
    @GetMapping("/type/{type}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByType(Pageable pageable, @PathVariable String type){
        Page<MovementResponseDto> movement = movementService.getMovementsByType(type, pageable);
        return ResponseEntity.ok(movement);
    }

    @Operation(
        summary = "Get movements by description",
        description = "Return a pageable with all movements registred in system with the description",
        responses = {
            @ApiResponse(responseCode = "200", description = "Movements found"),
            @ApiResponse(responseCode = "404", description = "Movements not found")
        }
    )
    @GetMapping("/description/{description}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByDescription(Pageable pageable, @PathVariable String description){
        Page<MovementResponseDto> movement = movementService.getMovementsByDescription(description, pageable);
        return ResponseEntity.ok(movement);
    }

    @Operation(
        summary = "Get movement by id",
        description = "Return a movement by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "Movement found"),
            @ApiResponse(responseCode = "404", description = "Movement not found")
        }
    )
    @GetMapping("/product/{productId}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByDProductId(Pageable pageable, @PathVariable Long productId){
        Page<MovementResponseDto> movement = movementService.getMovementsByProductId(productId, pageable);
        return ResponseEntity.ok(movement);
    }

    @Operation(
        summary = "Get movements by date",
        description = "Return a pageable with all movements between the dates",
        responses = {
            @ApiResponse(responseCode = "200", description = "Movements found"),
            @ApiResponse(responseCode = "404", description = "Movements not found")
        }
    )
    @GetMapping("/date/{startDate}/{endDate}")
    public ResponseEntity<Page<MovementResponseDto>> getMovementsByDate(
            Pageable pageable,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        Page<MovementResponseDto> movement = movementService.getMovementsByDate(startDate, endDate, pageable);
        return ResponseEntity.ok(movement);
    }

    
}
