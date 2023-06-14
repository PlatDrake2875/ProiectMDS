package Proiect.MDS.web.controller;

import Proiect.MDS.web.dto.RecipeDto;
import Proiect.MDS.web.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller

public class RecipeController {
    private RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @RequestMapping("/recipes")
    public String listRecipes(Model model) {
        List<RecipeDto> recipes = service.getAllRecipes();
        model.addAttribute("recipes", recipes);
        return "recipes-list";

    }
}
