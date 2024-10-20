package com.exam.license.exam.repository;

import com.exam.license.exam.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
