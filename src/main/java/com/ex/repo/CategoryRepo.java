package com.ex.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.domain.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer> {
	public Category findOneByCategoryName(String categoryName);
	List<Category> findByCategoryNameIgnoreCaseContaining(String categoryName);
}
