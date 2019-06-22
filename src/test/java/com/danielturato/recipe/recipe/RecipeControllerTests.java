package com.danielturato.recipe.recipe;

import com.danielturato.recipe.Application;
import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.core.WebSecurity;
import com.danielturato.recipe.core.WithMockCustomUser;
import com.danielturato.recipe.ingredient.Ingredient;
import com.danielturato.recipe.ingredient.IngredientService;
import com.danielturato.recipe.ingredient.IngredientServiceImpl;
import com.danielturato.recipe.recipe.Recipe;
import com.danielturato.recipe.recipe.RecipeController;
import com.danielturato.recipe.recipe.RecipeService;
import com.danielturato.recipe.user.DetailsService;
import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserService;
import com.danielturato.recipe.user.UserServiceImpl;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class RecipeControllerTests {

	private MockMvc mockMvc;

	@MockBean
	private RecipeServiceImpl recipeService;

	@MockBean
	private UserServiceImpl userService;

	@MockBean
	private IngredientServiceImpl ingredientService;

	@Autowired
	WebApplicationContext wac;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).apply(springSecurity()).build();
	}

	/**
	 * Tests for index pages / & /recipes
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void indexPageLoads() throws Exception {
		List<Recipe> recipes = recipeListBuilder();
		List<Ingredient> ingredients = ingredientsListBuilder();

		when(recipeService.findAll()).thenReturn(recipes);
		when(ingredientService.findAll()).thenReturn(ingredients);
		when(userService.findByUsername("daniel")).thenReturn(userBuilder());

		mockMvc.perform(get("/recipes"))
				.andExpect(model().attributeExists("recipes", "ingredients", "favs"))
				.andExpect(status().isOk());

		verify(recipeService, times(1)).findAll();
		verify(ingredientService, times(1)).findAll();
		verify(userService, times(1)).findByUsername(any(String.class));

	}

	/**
	 * Tests for page /recipes/add
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void addRecipePageLoads() throws Exception {
		mockMvc.perform(get("/recipes/add"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task", "buttonAction", "action", "photo", "recipe"));
	}

	@Test
	@WithMockCustomUser(username = "daniel")
	public void createNewRecipeRedirects() throws Exception {
		User user = userBuilder();
		MockMultipartFile photo = new MockMultipartFile("image", "food.jpeg",
				"image/jpeg", "dummy content file".getBytes());

		when(userService.findByUsername("daniel")).thenReturn(user);
		doAnswer(invocation -> {
			Recipe recipe = (Recipe)invocation.getArgument(0);
			MultipartFile file = (MultipartFile)invocation.getArgument(1);
			recipe.setPhoto(file.getBytes());
			recipe.setId(1L);
			return null;
		}).when(recipeService).save(any(Recipe.class), any(MultipartFile.class));

		doAnswer(invocation -> null).when(userService).save(any(User.class));

		mockMvc.perform(multipart("/recipes/add")
						.file(photo)
						.param("name", "testRecipe")
						.param("description", "testDesc")
						.param("category", Category.BREAKFAST.name()))
				.andDo(print())
				.andExpect(redirectedUrl("/recipes"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("flash"));

		verify(recipeService, times(1)).save(any(Recipe.class), any(MultipartFile.class));
		verify(userService, times(1)).save(any(User.class));
	}

	@Test
	@WithMockUser
	public void errorWithRecipeCreationRedirectsBack() throws Exception {
		MockMultipartFile photo = new MockMultipartFile("image", "food.jpeg",
				"image/jpeg", "dummy content file".getBytes());

		mockMvc.perform(multipart("/recipes/add")
					.file(photo)
					.param("description", "testDesc")
					.param("category", Category.BREAKFAST.name()))
				.andDo(print())
				.andExpect(redirectedUrl("/recipes/add"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("flash"));
	}

	@Test
	@WithMockCustomUser(username = "daniel")
	public void editRecipePageLoadsWithRecipeOwner() throws Exception {
		Recipe recipe = recipeBuilder(1L);
		User user = userBuilder();
		recipe.setOwner(user);

		when(recipeService.findById(1L)).thenReturn(recipe);

		mockMvc.perform(get("/recipes/1/edit"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task", "buttonAction", "action", "recipe"));

		verify(recipeService, times(2)).findById(any(Long.class));
	}

	private User userBuilder() {
		User user = new User();
		user.setId(1L);
		user.setRoles(new String[]{"ROLE_USER", "ROLE_ADMIN"});
		user.setUsername("daniel");
		user.setPassword("password");

		return user;
	}

	private List<Recipe> recipeListBuilder() {
		List<Recipe> recipes =  new ArrayList<>();
		recipes.add(recipeBuilder(1L));
		recipes.add(recipeBuilder(2L));

		return recipes;
	}

	private List<Ingredient> ingredientsListBuilder() {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(ingredientBuilder());

		return ingredients;
	}

	private Ingredient ingredientBuilder() {
		Ingredient ingredient = new Ingredient();
		ingredient.setCondition("good");
		ingredient.setName("test ing");
		ingredient.setQuantity(1);
		ingredient.setId(1L);

		return ingredient;
	}

	private Recipe recipeBuilder(Long id) {
		Recipe recipe = new Recipe();
		recipe.setName("Test recipe");
		recipe.setDescription("Test Description");
		recipe.setId(id);
		recipe.setCategory(Category.ALL_CATEGORIES);
		recipe.setCookTime(10);
		recipe.setPrepTime(10);
		recipe.addIngredient(ingredientBuilder());
		recipe.setOwner(userBuilder());

		return recipe;
	}
}
