package com.danielturato.recipe.recipe;

import com.danielturato.recipe.core.BaseTest;
import com.danielturato.recipe.exception.RecipeNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeServiceTests extends BaseTest {

    @TestConfiguration
    static class RecipeServiceImplTestContextConfiguration {

        @Bean
        public RecipeService recipeService() {
            return new RecipeServiceImpl();
        }
    }

    @Autowired
    private RecipeService recipeService;

    @MockBean
    private RecipeRepository recipeRepository;

    private Recipe recipe;

    @Before
    public void setUp() throws Exception {
        recipe = recipeBuilder(1L);
    }

    @Test
    public void saveRecipeWithBytesArray() throws Exception {
        doAnswer(invocation -> null).when(recipeRepository).save(recipe);
        recipeService.save(recipe, recipe.getPhoto());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    public void saveRecipeWithMultipartPhoto() throws Exception {
        doAnswer(invocation -> null).when(recipeRepository).save(recipe);
        recipeService.save(recipe, getPhoto());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    public void findRecipeWithIdNotNull() throws Exception {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        recipeService.findById(1L);
        verify(recipeRepository).findById(any(Long.class));
    }

    @Test(expected = RecipeNotFoundException.class)
    public void findRecipeWithIdThatsNull() throws Exception {
        when(recipeRepository.findById(3L)).thenReturn(Optional.empty());
        recipeService.findById(3L);
        verify(recipeRepository).findById(any(Long.class));
    }

    @Test
    public void findAllRecipes() throws Exception {
        when(recipeRepository.findAll()).thenReturn(new ArrayList<Recipe>());
        recipeService.findAll();
        verify(recipeRepository).findAll();
    }

    @Test
    public void deleteRecipeById() throws Exception {
        doAnswer(invocation -> null).when(recipeRepository).deleteById(any(Long.class));
        recipeService.deleteById(1L);
        verify(recipeRepository).deleteById(any(Long.class));
    }

}
