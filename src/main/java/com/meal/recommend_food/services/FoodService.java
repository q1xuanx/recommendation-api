package com.meal.recommend_food.services;

import com.meal.recommend_food.components.AddDataComponent;
import com.meal.recommend_food.entities.Food;
import com.meal.recommend_food.repositories.FoodRepository;
import com.meal.recommend_food.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.example.Pair;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    public void addData(List<String> data){
        List<Food> foods = new ArrayList<>();
        for (String s : data){
            Food food = foodRepository.findByNameOfFood(s);
            if (food == null){
                Food f = new Food();
                f.setNameOfFood(s);
                foods.add(f);
            }
        }
        foodRepository.saveAll(foods);
    }
    public ApiResponse searchData(String inputs){
        List<String> splitArray = Arrays.stream(inputs.split(",")).toList();
        Map<String, List<Food>> outputs = new HashMap<>();
        for (String s : splitArray){
            outputs.putIfAbsent(s, new ArrayList<>());
            List<Food> foods = foodRepository.findAll().stream().filter(food -> food.getNameOfFood().toLowerCase().contains(s.toLowerCase())).toList();
            outputs.put(s, foods);
        }
        if (outputs.isEmpty()){
            return new ApiResponse(200, "not found", null);
        }
        return new ApiResponse(200, "find success", outputs);
    }
    public ApiResponse recommendData(List<Integer> inputFood) throws InterruptedException {
        Map<Pair<List<Integer>, Double>, Map<List<Integer>, Map<Integer,Double>>> dataTrain =  AddDataComponent.recommendModel.trainConstraint(AddDataComponent.dataNumber, inputFood);
        Map<Integer, Double> result;
        System.out.println(dataTrain);
        System.out.println(inputFood);
        if (dataTrain.get(new Pair<>(inputFood, (double) 0.0f)) == null) {
            result = AddDataComponent.recommendModel.recommendConstraint(dataTrain);
        } else {
            result = AddDataComponent.recommendModel.recommendPairwise(AddDataComponent.trainedPairwise, inputFood, 2.0f);
        }
        Map<String, Double> finalResult = new HashMap<>();
        for (Map.Entry<Integer, Double> entry : result.entrySet()){
            finalResult.put(AddDataComponent.reverseMapping.get(entry.getKey()), entry.getValue());
        }
        return new ApiResponse(200, "data recommended", finalResult);
    }
}
