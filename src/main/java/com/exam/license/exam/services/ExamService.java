package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.models.Score;
import com.exam.license.exam.models.UserAnswer;
import com.exam.license.exam.models.UserScore;
import com.exam.license.exam.repository.CategoryRepository;
import com.exam.license.exam.repository.QuestionRepository;
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

    public List<Question> getQuestionsForExam(String categoryName) throws NotEnoughtQuestionsException, NoSuchElementInDatabaseException {
        List<Question> exam = new ArrayList<>();
        categoryRepository.findCategoryByName(categoryName).orElseThrow(NoSuchElementInDatabaseException::new);
        exam.addAll(fetchSpecificQuestionType(10, categoryName, 3, false));
        exam.addAll(fetchSpecificQuestionType(6, categoryName, 2, false));
        exam.addAll(fetchSpecificQuestionType(4, categoryName, 1, false));
        exam.addAll(fetchSpecificQuestionType(6, categoryName, 3, true));
        exam.addAll(fetchSpecificQuestionType(4, categoryName, 2, true));
        exam.addAll(fetchSpecificQuestionType(2, categoryName, 1, true));
        return exam;
    }

    public List<Question> fetchSpecificQuestionType(int limit, String categoryName, int point, boolean isSpecialization) throws NotEnoughtQuestionsException {
        List<Question> questions;
        if (isSpecialization) {
            questions = this.questionRepository.findNBasicQuestionWithCategoryAndPoints(limit,
                    categoryName, point);
        } else {
            questions = this.questionRepository.findNSpecializationQuestionWithCategoryAndPoints(limit,
                    categoryName, point);
        }
        if (questions.size() < limit) {
            throw new NotEnoughtQuestionsException();
        }
        return questions;
    }

    public Score checkUserSolution(List<UserAnswer> userSolution) throws NoSuchElementInDatabaseException {
        UserScore score = new UserScore();
        for (UserAnswer userAnswer : userSolution) {
            score.addScore(this.checkAnswer(userAnswer));
        }
        return score;
    }

    public Score checkAnswer(UserAnswer userAnswer) throws NoSuchElementInDatabaseException {
        Question question = this.questionRepository.findById(Long.valueOf(userAnswer.getQuestionId())).orElseThrow(NoSuchElementInDatabaseException::new);
        Score questionScore = new Score();
        questionScore.setOf(question.getPoints());
        if (question.getAnswerCorrect().equals(userAnswer.getSelectedAnswer())) {
            questionScore.setPoints(question.getPoints());

        } else {
            questionScore.setPoints(0);
        }
        return questionScore;
    }
}
