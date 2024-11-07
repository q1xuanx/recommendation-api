package com.meal.recommend_food.services;

import com.meal.recommend_food.entities.Food;
import com.meal.recommend_food.repositories.FoodRepository;
import com.meal.recommend_food.response.ApiResponse;
import com.meal.recommend_food.utils.Pair;
import lombok.RequiredArgsConstructor;
import org.example.RecommendModel;
import org.springframework.stereotype.Service;
import java.io.IOException;
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
    public ApiResponse recommendData(List<String> inputFood) throws IOException, ClassNotFoundException {
        RecommendModel model = new RecommendModel();
        model.loadModel(getClass().getResourceAsStream("/modelRecommend.ser"));
        Map<String, Double> recommend = model.recommend(inputFood);
        List<Pair<String, Double>> data = new ArrayList<>();
        for (Map.Entry<String, Double> entry : recommend.entrySet()){
            data.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        int k = data.size();
        Random random = new Random();
        for (int i = 0; i < data.size(); i++){
            int index = random.nextInt(k);
            Collections.swap(data, i, index);
            k -= 1;
            if (k == 0){
                break;
            }
        }
        return new ApiResponse(200, "data recommended", data.stream().limit(10));
    }
}
