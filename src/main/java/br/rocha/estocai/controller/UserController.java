package br.rocha.estocai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import br.rocha.estocai.model.dtos.UserPatchDto;
import br.rocha.estocai.model.dtos.UserResponseDto;
import br.rocha.estocai.model.dtos.UserRoleRequest;
import br.rocha.estocai.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
@Tag(name = "User", description = "Operations about users")
public class UserController {
    @Autowired
    UserService userService;

    @Operation(
        summary = "Update user informations",
        description = "Update a users informations if is present",
        responses = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUserPartial(@PathVariable Long id, @RequestBody @Valid UserPatchDto data) {
        UserResponseDto user = userService.updateUserPartial(id, data);
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Update user roles",
        description = "Update a user role for admin or user by id",
        responses = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @PatchMapping("/roles/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> updateUserRole(@PathVariable Long id, @RequestBody UserRoleRequest role) {
        UserResponseDto user = userService.updateRole(id, role.role());
        return ResponseEntity.ok(user);
    }

    @Operation(
        summary = "Delete a user",
        description = "Delete a user by id",
        responses = {
            @ApiResponse(responseCode = "204", description = "User deleted, no content more"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Requisition")
        }
    )
    @PreAuthorize("#id == principal.id or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
    }
    
}
