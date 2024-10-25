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
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final long limit = 14;

    @Autowired
    public ExamService(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }
// throws NoSuchDataInDatabaseException
    //NoSuchDataInDatabaseException::new
    public List<Question> getQuestionsForExam(String categoryName){
        Category category = this.categoryRepository.findCategoryByName(categoryName).orElse(null);
        List<Question> questionList = this.questionRepository.findNQuestionWithCategory(this.limit, category);
        return questionList;
    }
}
