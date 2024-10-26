package com.exam.license.exam.repository;

import com.exam.license.exam.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
   @Query(value = """
            SELECT q.* FROM question q
            JOIN category_connection cc ON q.id = cc.question_id
            JOIN category c ON cc.category_id = c.id
            WHERE q.points = :ptn AND q.answers_id IS NULL AND c.name = :ctn
            ORDER BY RANDOM()
            LIMIT :l
            """, nativeQuery = true)
    List<Question> findNBasicQuestionWithCategoryAndPoints(@Param("l") long limit, @Param("ctn") String category, @Param("ptn") int points);

    @Query(value = """
            SELECT q.* FROM question q
            JOIN category_connection cc ON q.id = cc.question_id
            JOIN category c ON cc.category_id = c.id
            WHERE q.points = :ptn AND q.answers_id IS NOT NULL AND c.name = :ctn
            ORDER BY RANDOM()
            LIMIT :l
            """, nativeQuery = true)
    List<Question> findNSpecializationQuestionWithCategoryAndPoints(@Param("l") long limit, @Param("ctn") String category, @Param("ptn") int points);
}
