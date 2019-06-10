package com.danielturato.recipe.recipe;

import com.danielturato.recipe.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    @RequestMapping(path = "/add-recipe", method = RequestMethod.GET)
    public String addRecipe(Model model) {
        model.addAttribute("task", "Add recipe");
        model.addAttribute("buttonAction", "Add");

        if (!model.containsAttribute("recipe")) {
            model.addAttribute("recipe", new Recipe());
        }

        return "edit";
    }

    @RequestMapping(path = "/add-recipe", method = RequestMethod.POST)
    public String persistRecipe(@Valid Recipe recipe) {
        recipe.setOwner(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        recipeService.save(recipe);
        return "redirect:/";
    }


}
