package br.rocha.estocai.Movement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import br.rocha.estocai.mappers.MovementMapper;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;
import br.rocha.estocai.repository.MovementRepository;
import br.rocha.estocai.service.MovementService;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTest {
    @InjectMocks
    private MovementService service;

    @Mock
    private MovementRepository movementRepository;

    @Mock 
    private MovementMapper mapper;

    @Mock
    private Product product;

    @Mock 
    private Category category;

    @Test
    void createProduct_ValidProduct(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Movement movement = new Movement(product, new Date(), MovementType.ENTRY, MovementDescription.ADDED);

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        service.createProduct(product);

        verify(movementRepository, times(1)).save(argThat(savedMovement ->
            savedMovement.getProduct().equals(product)
                && savedMovement.getType() == MovementType.ENTRY
                && savedMovement.getDescription() == MovementDescription.ADDED
        ));
    }

    @Test
    void createProduct_NullProduct_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> service.createProduct(null));
    }

    @Test
    void createProduct_WhenRepositoryFails_ShouldPropagateException() {
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        when(movementRepository.save(any(Movement.class)))
            .thenThrow(new RuntimeException("Database error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.createProduct(product));
        assertEquals("Database error", ex.getMessage());
    }

    @Test
    void createProduct_ProductWithoutCategory_ShouldStillSave() {
        Product product = new Product("Product", "Description", 11.99, 12, null);
        ReflectionTestUtils.setField(product, "id", 1L);

        when(movementRepository.save(any(Movement.class))).thenReturn(new Movement());

        service.createProduct(product);

        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void removeProduct_ValidProduct(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Movement movement = new Movement(product, new Date(), MovementType.EXIT, MovementDescription.REMOVED);

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        service.removeProduct(product);

        verify(movementRepository, times(1)).save(argThat(savedMovement ->
            savedMovement.getProduct().equals(product)
                && savedMovement.getType() == MovementType.EXIT
                && savedMovement.getDescription() == MovementDescription.REMOVED
        ));
    }

    @Test
    void decreaseQuantity_ValidProduct(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Movement movement = new Movement(product, new Date(), MovementType.EXIT, MovementDescription.QUANTITY_DECREASED);

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        service.decreaseQuantity(product);

        verify(movementRepository, times(1)).save(argThat(savedMovement ->
            savedMovement.getProduct().equals(product)
                && savedMovement.getType() == MovementType.EXIT
                && savedMovement.getDescription() == MovementDescription.QUANTITY_DECREASED
        ));
    }

    @Test
    void increaseQuantity_ValidProduct(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Movement movement = new Movement(product, new Date(), MovementType.ENTRY, MovementDescription.QUANTITY_INCREASED);

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        service.increaseQuantity(product);

        verify(movementRepository, times(1)).save(argThat(savedMovement ->
            savedMovement.getProduct().equals(product)
                && savedMovement.getType() == MovementType.ENTRY
                && savedMovement.getDescription() == MovementDescription.QUANTITY_INCREASED
        ));
    }

    @Test
    void updateProduct_ValidProduct(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Movement movement = new Movement(product, new Date(), MovementType.EDITED, MovementDescription.EDITED);

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        service.updateProduct(product);

        verify(movementRepository, times(1)).save(argThat(savedMovement ->
            savedMovement.getProduct().equals(product)
                && savedMovement.getType() == MovementType.EDITED
                && savedMovement.getDescription() == MovementDescription.EDITED
        ));
    }

    @Test
    void consultProduct_ValidProduct(){
        Category category = new Category("Category", "Description");
        ReflectionTestUtils.setField(category, "id", 1L);

        Product product = new Product("Product", "Description", 11.99, 12, category);
        ReflectionTestUtils.setField(product, "id", 1L);

        Movement movement = new Movement(product, new Date(), MovementType.NONE, MovementDescription.CONSULT);

        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        service.consultProduct(product);

        verify(movementRepository, times(1)).save(argThat(savedMovement ->
            savedMovement.getProduct().equals(product)
                && savedMovement.getType() == MovementType.NONE
                && savedMovement.getDescription() == MovementDescription.CONSULT
        ));
    }

    @Test
    void registerMovement_WithNullType_ShouldThrowException() throws Exception {
        Category category = new Category("Category", "Description");
        Product product = new Product("Product", "Description", 11.99, 12, category);

        var method = MovementService.class.getDeclaredMethod("registerMovement", Product.class, MovementType.class, MovementDescription.class);
        method.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(service, product, null, MovementDescription.ADDED);
        });

        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }


}
