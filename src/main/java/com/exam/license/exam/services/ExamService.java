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
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@Service
@SessionScope
public class ExamService {
    //todo implement paggination over questions
    //todo make view for exam
    //todo error with category set
    //todo do i realy need category set in question model ? if yes why?
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    @Lazy
    private List<Question> exam = new ArrayList<>();
    private int questionIdx = 0;

    @Autowired
    public ExamService(QuestionRepository questionRepository, CategoryRepository categoryRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
    }

    public void createExam(String categoryName) throws NotEnoughtQuestionsException, NoSuchElementInDatabaseException {
        categoryRepository.findCategoryByName(categoryName).orElseThrow(NoSuchElementInDatabaseException::new);
        this.exam.addAll(fetchQuestionType(10, categoryName, 3, false));
        this.exam.addAll(fetchQuestionType(6, categoryName, 2, false));
        this.exam.addAll(fetchQuestionType(4, categoryName, 1, false));
        Collections.shuffle(exam);
        List<Question> specializationPart = new ArrayList<>();
        specializationPart.addAll(fetchQuestionType(6, categoryName, 3, true));
        specializationPart.addAll(fetchQuestionType(4, categoryName, 2, true));
        specializationPart.addAll(fetchQuestionType(2, categoryName, 1, true));
        Collections.shuffle(specializationPart);
        this.exam.addAll(specializationPart);
    }

    public Question getNextQuestion(){
        Question question = null;
        if(this.questionIdx<this.exam.size()){
            question = this.exam.get(this.questionIdx);
            this.questionIdx++;
        }
        return question;
    }

    public List<Question> fetchQuestionType(int limit, String categoryName, int point, boolean isSpecialization) throws NotEnoughtQuestionsException {
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
