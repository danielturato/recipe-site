package com.danielturato.recipe.recipe;

import com.danielturato.recipe.user.User;
import com.danielturato.recipe.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

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
        return "index";
    }

    @RequestMapping(path = "/recipes/add", method = RequestMethod.GET)
    public String addRecipe(Model model) {
        model.addAttribute("task", "Add recipe");
        model.addAttribute("buttonAction", "Add");

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

    @RequestMapping(path = "/recipes/{id}", method = RequestMethod.GET)
    public String getRecipe(@PathVariable Long id, Model model) {
        Recipe recipe = recipeService.findById(id);
        model.addAttribute("recipe", recipe);

        return "detail";
    }

    @RequestMapping("/recipes/{id}.png")
    @ResponseBody
    public byte[] recipeImage(@PathVariable Long id) {
        return recipeService.findById(id).getPhoto();
    }


}
