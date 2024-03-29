package com.danielturato.recipe.recipe;

import com.danielturato.recipe.category.Category;
import com.danielturato.recipe.flash.FlashMessage;
import com.danielturato.recipe.ingredient.Ingredient;
import com.danielturato.recipe.ingredient.IngredientServiceImpl;
import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RecipeController {

    private RecipeServiceImpl recipeService;
    private UserServiceImpl userService;
    private IngredientServiceImpl ingredientService;

    @Autowired
    public RecipeController(RecipeServiceImpl recipeService, UserServiceImpl userService, IngredientServiceImpl ingredientService) {
        this.recipeService = recipeService;
        this.userService = userService;
        this.ingredientService = ingredientService;
    }

    @GetMapping({"/", "/recipes"})
    public String index(Model model) {
        if (!model.containsAttribute("recipes")) {
            model.addAttribute("recipes", recipeService.findAll());
        }
        if (!model.containsAttribute("queryObject")) {
            model.addAttribute("queryObject", new RecipeQuery());
        }

        model.addAttribute("favs", getUser().getFavorites());
        model.addAttribute("ingredients", ingredientService.findAll());

        return "index";
    }

    @RequestMapping(path = "/recipes/add", method = RequestMethod.GET)
    public String addRecipe(Model model) {
        model.addAttribute("task", "Add recipe");
        model.addAttribute("buttonAction", "Add");
        model.addAttribute("action", "/recipes/add");
        model.addAttribute("photo", false);

        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", new Recipe());
        }

        return "edit";
    }

    @RequestMapping(path = "/recipes/add", method = RequestMethod.POST)
    public String persistRecipe(@Valid Recipe recipe, BindingResult result, @RequestParam("image") MultipartFile photo, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(System.out::println);
            redirectAttributes.addFlashAttribute("recipe", recipe);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("I think you missed something. Try again!", FlashMessage.Status.FAILURE));
            return "redirect:/recipes/add";
        }

        User user = getUser();
        recipe.setOwner(user);
        user.addFavorite(recipe);
        recipeService.save(recipe, photo);
        userService.save(user);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The recipe has successfully been created", FlashMessage.Status.SUCCESS));

        return "redirect:/recipes";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @recipeServiceImpl?.findById(#id)?.owner.username == authentication.name")
    @RequestMapping(path = "/recipes/{id}/edit", method = RequestMethod.GET)
    public String editRecipe(Model model, @PathVariable("id") Long id) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("task", "Edit recipe");
        model.addAttribute("buttonAction", "Save");
        model.addAttribute("action", String.format("/recipes/%d/edit", id));

        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", recipe);
        }

        if (recipe.getPhoto() != null) {
            model.addAttribute("photo", true);
        }

        return "edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @recipeServiceImpl?.findById(#id)?.owner.username == authentication.name")
    @RequestMapping(path = "/recipes/{id}/edit", method = RequestMethod.POST)
    public String updateRecipe(@Valid Recipe recipe, BindingResult result, @PathVariable("id") Long id,
                               @RequestParam(required = false, value = "image") MultipartFile photo,
                               RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("recipe", recipe);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage("I think you missed something. Try again!", FlashMessage.Status.FAILURE));
            return String.format("/redirect:/recipes/%d/edit", id);
        }

        Recipe original = recipeService.findById(id);
        recipe.setOwner(original.getOwner());

        if (photo == null) {
            recipeService.save(recipe, original.getPhoto());
        } else {
            recipeService.save(recipe, photo);
        }
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The recipe has successfully been edited", FlashMessage.Status.SUCCESS));

        return String.format("redirect:/recipes/%d", id);
    }


    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public String getRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        User user = getUser();
        model.addAttribute("recipe", recipe);
        if (user.isAFavorite(recipe)) {
            model.addAttribute("fav", true);
        } else {
            model.addAttribute("fav", false);
        }


        return "detail";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @recipeServiceImpl?.findById(#id)?.owner.username == authentication.name")
    @RequestMapping(path = "/recipes/{id}/delete", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        removeFavorites(recipeService.findById(id));
        recipeService.deleteById(id);
        redirectAttributes.addFlashAttribute("flash", new FlashMessage("The recipe has successfully been deleted", FlashMessage.Status.SUCCESS));


        return "redirect:/recipes";
    }

    @RequestMapping(path = "/recipes/{id}/favorite", method = RequestMethod.POST)
    public String toggleFavorite(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        User user = getUser();
        Recipe recipe = recipeService.findById(id);
        if (user.isAFavorite(recipe)) {
            user.removeFavorite(recipe);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage(String.format("%s has been removed from your favorites", recipe.getName()), FlashMessage.Status.SUCCESS));
        } else {
            user.addFavorite(recipe);
            redirectAttributes.addFlashAttribute("flash",
                    new FlashMessage(String.format("%s has been added to your favorites", recipe.getName()), FlashMessage.Status.SUCCESS));
        }
        userService.save(user);


        return String.format("redirect:/recipes/%d", id);
    }

    @RequestMapping(path = "/recipes/search", method = RequestMethod.POST)
    public String searchQuery(RecipeQuery query, RedirectAttributes attributes) {
        List<Recipe> recipes = recipeService.queryRecipes(query);

        attributes.addFlashAttribute("recipes", recipes);
        attributes.addFlashAttribute("queryObject", query);

        return "redirect:/recipes";
    }

    @RequestMapping(path = "/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

    @RequestMapping("/recipes/{id}.png")
    @ResponseBody
    public byte[] recipeImage(@PathVariable Long id) {
        return recipeService.findById(id).getPhoto();
    }

    private User getUser() {
        return userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private void removeFavorites(Recipe recipe) {
        for (User user : userService.findAll()) {
            List<Recipe> favs = user.getFavorites();
            if (favs.contains(recipe)) {
                favs.remove(recipe);
                user.setFavorites(favs);
                userService.save(user);
            }
        }
    }

}
