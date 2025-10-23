package br.rocha.estocai.Category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import br.rocha.estocai.model.Category;
import br.rocha.estocai.repository.CategoryRepository;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;
    
    @BeforeEach
    void setup() {
        categoryRepository.deleteAll();

        Category category = new Category();
        category.setName("Category");
        category.setDescription("Description");
        categoryRepository.save(category);
    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryById() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        mockMvc.perform(get("/categories/" + category.getId())
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.id").value(category.getId()));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getCategoryByName() throws Exception {
        Category category = categoryRepository.findAll().get(0);

        mockMvc.perform(get("/categories/name/" + category.getName())
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Category"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.id").value(category.getId()));

    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getAllCategories() throws Exception {
        List<Category> categoryList = categoryRepository.findAll();
        Page<Category> page = new PageImpl<>(categoryList);

        mockMvc.perform(get("/categories")
                .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Category"))
                .andExpect(jsonPath("$.content[0].description").value("Description"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory() throws Exception {
        String json = """
                    {
                        "name": "Category New",
                        "description": "Description New"
                    }
                """;

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Category New"))
            .andExpect(jsonPath("$.description").value("Description New"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_ConflictName() throws Exception {
        String json = """
                    {
                        "name": "Category",
                        "description": "Description"
                    }
                """;

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isConflict());
    }
    

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCategory_BlankParam() throws Exception {
        String json = """
                    {
                        "name": "Category Super New",
                        "description": 
                    }
                """;

        mockMvc.perform(post("/categories")
                .contentType("application/json")
                .content(json))
            .andDo(print())
            .andExpect(status().isBadRequest());
    }

    
}
