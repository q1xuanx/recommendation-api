package com.meal.recommend_food.controllers;


import com.meal.recommend_food.response.ApiResponse;
import com.meal.recommend_food.services.FoodService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FoodController {
    private final FoodService foodService;
    @GetMapping("/search-food")
    public ApiResponse searchFood(@RequestParam String search) {
        return foodService.searchData(search);
    }
    @PostMapping("/recommend-food")
    public ApiResponse recommendFood(@RequestBody List<String> foodChoice) throws IOException, ClassNotFoundException {
        return foodService.recommendData(foodChoice);
    }
}
