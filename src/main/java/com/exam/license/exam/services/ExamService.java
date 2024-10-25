package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.NoSuchCategoryInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Category;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.repository.CategoryRepository;
import com.exam.license.exam.repository.QuestionRepository;
import com.exam.license.exam.selector.QuestionSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final int limit = 14;

    @Autowired
    public ExamService(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Question> getQuestionsForExam(String categoryName) throws NotEnoughtQuestionsException, NoSuchCategoryInDatabaseException {
        Category category = this.categoryRepository.findCategoryByName(categoryName).orElseThrow(NoSuchCategoryInDatabaseException::new);
        ArrayList<Question> questions = new ArrayList<>(category.getQuestionSet());
        QuestionSelector questionSelector = new QuestionSelector();
        List<Question> subset = questionSelector.selectQuestionsFromSet(questions, limit);
        return subset;
    }
}
