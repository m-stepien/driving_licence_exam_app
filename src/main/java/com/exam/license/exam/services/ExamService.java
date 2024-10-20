package com.exam.license.exam.services;

import com.exam.license.exam.models.Category;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.repository.CategoryRepository;
import com.exam.license.exam.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamService {
    private QuestionRepository questionRepository;
    private CategoryRepository categoryRepository;
    private final long limit = 14;

    @Autowired
    public ExamService(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Question> getRandomQuestionForExam(String categoryName){
        Category category = this.categoryRepository.findCategoryByName(categoryName).orElse(null);
        System.out.println("INFOOO");
        System.out.println(category.getName());
        List<Question> questionList = this.questionRepository.findNQuestionWithCategory(this.limit, category);
        System.out.println(questionList.size());
        return questionList;
    }
}
