package br.rocha.estocai.Product;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import br.rocha.estocai.model.Category;
import br.rocha.estocai.model.Product;
import br.rocha.estocai.repository.CategoryRepository;
import br.rocha.estocai.repository.MovementRepository;
import br.rocha.estocai.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MovementRepository movementRepository;


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
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnProduct_WhenGetById() throws Exception {
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(get("/products/" + product.getId())
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.id").value(product.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnProduct_WhenGetByName() throws Exception {
        Product product = productRepository.findAll().get(0);

        mockMvc.perform(get("/products/name/" + product.getName())
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product"))
            .andExpect(jsonPath("$.description").value("Description"))
            .andExpect(jsonPath("$.id").value(product.getId()));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturnAllProducts_WhenUserHasUserRole() throws Exception {
        mockMvc.perform(get("/products")
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Product"))
            .andExpect(jsonPath("$.content[0].description").value("Description"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnAllProducts_WhenUserHasAdminRole() throws Exception {
        mockMvc.perform(get("/products")
            .contentType("application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].name").value("Product"))
            .andExpect(jsonPath("$.content[0].description").value("Description"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldCreateProduct_WhenDataIsValid() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        String json = """
            {
                "name": "Product new",
                "description": "Description New",
                "price": 10.99,
                "quantity": 500,
                "categoryId": %d
            }
        """.formatted(category.getId());

        mockMvc.perform(post("/products")
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Product new"))
            .andExpect(jsonPath("$.description").value("Description New"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdateProduct_WhenDataIsValid() throws Exception {
        Product product = productRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        String json = """
            {
                "name": "Product Super New",
                "description": "Description Super New",
                "price": 10.89,
                "quantity": 590,
                "categoryId": %d
            }
        """.formatted(category.getId());

        mockMvc.perform(put("/products/" + product.getId())
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product Super New"))
            .andExpect(jsonPath("$.description").value("Description Super New"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldUpdatePartialProduct_WhenDataIsValid() throws Exception {
        Product product = productRepository.findAll().get(0);
        Category category = categoryRepository.findAll().get(0);

        String json = """
            {
                "name": "Product Is New",
                "description": "Description Is New",
                "price": 10.89
            }
        """.formatted(category.getId());

        mockMvc.perform(patch("/products/" + product.getId())
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product Is New"))
            .andExpect(jsonPath("$.description").value("Description Is New"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldIncreaseQuantity_WhenDataIsValid() throws Exception {
        Product product = productRepository.findAll().get(0);
        Integer quantity = product.getQuantity() + 1;

        String json = "1";

        mockMvc.perform(patch("/products/increaseQuantity/" + product.getId())
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Product"))
            .andExpect(jsonPath("$.quantity").value(quantity));
    }

}
