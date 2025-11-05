package br.rocha.estocai.Movement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.Movement;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.model.enums.MovementDescription;
import br.rocha.estocai.model.enums.MovementType;
import br.rocha.estocai.repository.CategoryRepository;
import br.rocha.estocai.repository.MovementRepository;
import br.rocha.estocai.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MovementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private MovementRepository movementRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setup() {
        movementRepository.deleteAll();
        categoryRepository.deleteAll(); 

        Category category = new Category();
        category.setName("Category");
        category.setDescription("Description");
        categoryRepository.save(category);

        Product product = new Product("Product", "Description", 90.00, 10, category);
        productRepository.save(product);

        Movement movement = new Movement(product, new Date(), MovementType.ENTRY, MovementDescription.ADDED);
        movementRepository.save(movement);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnAllMovements_WhenUserHasUserRole() throws Exception {
        mockMvc.perform(get("/movements")
                .content("application/json"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllMovements_WhenUserHasAdminRole() throws Exception {
        mockMvc.perform(get("/movements")
                .content("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.name").value("Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnMovementsByType_WhenUserHasAdminRole() throws Exception {
        String type = "entry";

        mockMvc.perform(get("/movements/type/" + type)
                .content("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.name").value("Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnMovementsByDescription_WhenUserHasAdminRole() throws Exception {
        String description = "added";

        mockMvc.perform(get("/movements/description/" + description)
                .content("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.name").value("Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnMovementsById_WhenUserHasAdminRole() throws Exception {
        Movement movement = movementRepository.findAll().get(0);
        Long id = movement.getProduct().getId();

        mockMvc.perform(get("/movements/product/" + id)
                .content("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.name").value("Product"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnMovementsByDate_WhenUserHasAdminRole() throws Exception {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = LocalDate.of(2025, 12, 31);

        mockMvc.perform(get("/movements/date/" + startDate + "/" + endDate)
                .content("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].product.name").value("Product"));
    }
}
