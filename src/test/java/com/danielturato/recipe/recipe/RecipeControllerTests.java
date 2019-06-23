package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.core.BaseTest;
import com.danielturato.recipe.core.WithMockCustomUser;
import com.danielturato.recipe.ingredient.Ingredient;
import com.danielturato.recipe.ingredient.IngredientServiceImpl;
import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserServiceImpl;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTests extends BaseTest {

	@MockBean
	private RecipeServiceImpl recipeService;

	@MockBean
	private UserServiceImpl userService;

	@MockBean
	private IngredientServiceImpl ingredientService;

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
				.andExpect(status().isOk())
				.andExpect(authenticated());

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

	/**
	 * Create new recipe post request & redirects to /recipes
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void createNewRecipeRedirects() throws Exception {
		User user = userBuilder();
		MockMultipartFile photo = getPhoto();

		when(userService.findByUsername("daniel")).thenReturn(user);
		doAnswer(invocation -> null).when(recipeService).save(any(Recipe.class), any(MultipartFile.class));

		doAnswer(invocation -> null).when(userService).save(any(User.class));

		mockMvc.perform(multipart("/recipes/add")
						.file(photo)
						.param("name", "testRecipe")
						.param("description", "testDesc")
						.param("category", Category.BREAKFAST.name()))
				.andExpect(redirectedUrl("/recipes"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("flash"))
				.andExpect(authenticated());

		verify(recipeService, times(1)).save(any(Recipe.class), any(MultipartFile.class));
		verify(userService, times(1)).save(any(User.class));
	}

	/**
	 * Form error redirects back to /recipes/add
	 */
	@Test
	@WithMockCustomUser
	public void errorWithRecipeCreationRedirectsBack() throws Exception {
		MockMultipartFile photo = getPhoto();

		mockMvc.perform(multipart("/recipes/add")
					.file(photo)
					.param("description", "testDesc")
					.param("category", Category.BREAKFAST.name()))
				.andExpect(authenticated())
				.andExpect(redirectedUrl("/recipes/add"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("flash"));
	}

	/**
	 * Edit recipe page loads if they are the recipe owner /recipe/{id}/edit
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void editRecipePageLoadsWithRecipeOwner() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);

		mockMvc.perform(get("/recipes/1/edit"))
				.andExpect(authenticated())
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("task", "buttonAction", "action", "recipe"));

		verify(recipeService, times(2)).findById(any(Long.class));
	}


	/**
	 * Edit recipe page loads if they are an admin and not recipe owner /recipe/{id}/edit
	 */
	@Test
	@WithMockCustomUser(username = "admin")
	public void editRecipePageLoadsWithNonOwnerWhoIsAdmin() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);

		mockMvc.perform(get("/recipes/1/edit"))
				.andExpect(status().isOk())
				.andExpect(authenticated())
				.andExpect(model().attributeExists("task", "buttonAction", "action", "recipe"));

		verify(recipeService, times(1)).findById(any(Long.class));
	}

	/**
	 * Edit recipe page's access is denied if they are not the recipe owner or an admin /recipe/{id}/edit
	 */
	@Test
	@WithMockCustomUser(username = "billy")
	public void editRecipePageAccessDeniedByNonOwnerAndNonAdmin() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);

		mockMvc.perform(get("/recipes/1/edit")).andDo(print())
				.andExpect(status().isForbidden())
				.andExpect(authenticated())
				.andExpect(forwardedUrl("/accessDenied"));

		verify(recipeService).findById(any(Long.class));

	}

	/**
	 * Edit recipe page post request keeping same photo redirects back to detail page
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void saveEditedRecipeKeepSamePhoto() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);

		doAnswer(invocation -> null).when(recipeService).save(recipe, recipe.getPhoto());

		mockMvc.perform(multipart("/recipes/1/edit")
					.param("name", "testRecipe")
					.param("description", "testDesc")
					.param("category", Category.BREAKFAST.name()))
				.andExpect(authenticated())
				.andExpect(redirectedUrl("/recipes/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("flash"));

		verify(recipeService).save(any(Recipe.class), any(byte[].class));
		verify(recipeService, times(2)).findById(any(Long.class));
	}

	/**
	 * Edit recipe page post request updating photo redirects back to detail page
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void saveEditedRecipeUpdatePhoto() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);

		doAnswer(invocation -> null).when(recipeService).save(recipe, getPhoto());

		mockMvc.perform(multipart("/recipes/1/edit")
					.file(getPhoto())
					.param("name", "testRecipe")
					.param("description", "testDesc")
					.param("category", Category.BREAKFAST.name()))
				.andExpect(authenticated())
				.andExpect(redirectedUrl("/recipes/1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(flash().attributeExists("flash"));

		verify(recipeService).save(any(Recipe.class), any(MultipartFile.class));
		verify(recipeService, times(2)).findById(any(Long.class));
	}

	/**
	 * Recipe detail page loads
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void recipeDetailPageLoads() throws Exception {
		User user = userBuilder();
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);
		when(userService.findByUsername("daniel")).thenReturn(user);

		mockMvc.perform(get("/recipes/1"))
				.andExpect(status().isOk())
				.andExpect(authenticated())
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService).findById(any(Long.class));
		verify(userService).findByUsername(any(String.class));
	}

	/**
	 * Delete a recipe as the recipe owner and redirect to /recipes
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void deleteRecipeAsRecipeOwner() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);
		when(userService.findAll()).thenReturn(new ArrayList<>());
		doAnswer(invocation -> null).when(recipeService).deleteById(1L);

		mockMvc.perform(post("/recipes/1/delete"))
				.andExpect(authenticated())
				.andExpect(flash().attributeExists("flash"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/recipes"));

		verify(recipeService, times(2)).findById(any(Long.class));
		verify(userService).findAll();
		verify(recipeService).deleteById(any(Long.class));
	}

	/**
	 * Delete a recipe as the recipe owner and redirect to /recipes
	 */
	@Test
	@WithMockCustomUser(username = "admin")
	public void deleteRecipeAsAdmin() throws Exception {
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);
		when(userService.findAll()).thenReturn(new ArrayList<>());
		doAnswer(invocation -> null).when(recipeService).deleteById(1L);

		mockMvc.perform(post("/recipes/1/delete"))
				.andExpect(authenticated())
				.andExpect(flash().attributeExists("flash"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/recipes"));

		verify(recipeService).findById(any(Long.class));
		verify(userService).findAll();
		verify(recipeService).deleteById(any(Long.class));
	}

	/**
	 * Favoring recipe redirects back to recipe detail
	 */
	@Test
	@WithMockCustomUser(username = "daniel")
	public void favoriteRecipeRedirectsBackToRecipe() throws Exception {
		User user = userBuilder();
		Recipe recipe = recipeBuilder(1L);

		when(recipeService.findById(1L)).thenReturn(recipe);
		when(userService.findByUsername("daniel")).thenReturn(user);
		doAnswer(invocation -> null).when(userService).save(user);

		mockMvc.perform(post("/recipes/1/favorite"))
				.andExpect(authenticated())
				.andExpect(flash().attributeExists("flash"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/recipes/1"));

		verify(recipeService).findById(any(Long.class));
		verify(userService).findByUsername(any(String.class));
		verify(userService).save(any(User.class));
	}
}
