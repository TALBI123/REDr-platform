package com.example.demo.agency.repository;

import com.example.demo.models.agency.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
