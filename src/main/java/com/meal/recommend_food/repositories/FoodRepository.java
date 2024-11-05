package com.meal.recommend_food.repositories;


import com.meal.recommend_food.entities.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    Food findByNameOfFood(String name);
}
