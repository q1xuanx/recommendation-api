package com.meal.recommend_food.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Pair<K,V> implements Serializable {
    private final K key;
    private final V value;
}
