package com.meal.recommend_food.response;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
public class ApiResponse {
    public int code;
    public String message;
    public Object data;
}
