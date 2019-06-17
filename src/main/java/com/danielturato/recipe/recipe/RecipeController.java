package com.danielturato.recipe.recipe;

import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RecipeController {

    private RecipeServiceImpl recipeService;
    private UserServiceImpl userService;

    @Autowired
    public RecipeController(RecipeServiceImpl recipeService, UserServiceImpl userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @GetMapping({"/", "/recipes"})
    public String index(Model model) {
        model.addAttribute("recipes", recipeService.findAll());
        model.addAttribute("favs", userService
                    .findByUsername(
                            SecurityContextHolder.getContext().getAuthentication().getName())
                    .getFavorites());
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
    public String persistRecipe(@Valid Recipe recipe, @RequestParam("image") MultipartFile photo) {
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        recipe.setOwner(user);
        user.addFavorite(recipe);
        recipeService.save(recipe, photo);
        userService.save(user);

        return "redirect:/recipes";
    }

    @RequestMapping(path = "/recipes/{id}/edit", method = RequestMethod.GET)
    public String editRecipe(Model model, @PathVariable Long id) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);
        model.addAttribute("task", "Edit recipe");
        model.addAttribute("buttonAction", "Save");
        model.addAttribute("action", String.format("/recipes/%d/edit", id));

        if (recipe.getPhoto() != null) {
            model.addAttribute("photo", true);
        }

        return "edit";
    }

    @RequestMapping(path = "/recipes/{id}/edit")
    public String updateRecipe(@Valid Recipe recipe, @PathVariable Long id,
                               @RequestParam(required = false, value = "image") MultipartFile photo) {
        if (photo == null) {
            recipeService.save(recipe, recipeService.findById(id).getPhoto());
        } else {
            recipeService.save(recipe, photo);
        }


        return String.format("redirect:/recipes/%d", id);
    }


    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public String getRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);

        return "detail";
    }

    @RequestMapping(path = "/recipes/{id}/delete", method = RequestMethod.POST)
    public String deleteRecipe(@PathVariable Long id) {
        removeFavorites(recipeService.findById(id));
        recipeService.deleteById(id);

        return "redirect:/recipes";
    }

    @RequestMapping("/recipes/{id}.png")
    @ResponseBody
    public byte[] recipeImage(@PathVariable Long id) {
        return recipeService.findById(id).getPhoto();
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
