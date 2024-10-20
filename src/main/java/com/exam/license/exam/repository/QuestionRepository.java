package com.exam.license.exam.repository;

import com.exam.license.exam.models.Category;
import com.exam.license.exam.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("SELECT q FROM Question q WHERE :ctn MEMBER OF q.categorySet ORDER BY random() LIMIT :l")
    List<Question> findNQuestionWithCategory(@Param("l") long limit, @Param("ctn") Category category);
}
