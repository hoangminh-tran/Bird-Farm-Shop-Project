package com.tttm.birdfarmshop.Repository;

import com.tttm.birdfarmshop.Models.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, String> {
}
