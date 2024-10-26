package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.*;
import com.exam.license.exam.repository.CategoryRepository;
import com.exam.license.exam.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Question> questions = this.questionRepository.findNBasicQuestionWithCategoryAndPoints(10, categoryName, 3);
        if(questions.size()<10){
            throw new NotEnoughtQuestionsException();
        }
        return questions;
    }

    public Score checkUserSolution(List<UserAnswer> userSolution) throws NoSuchElementInDatabaseException{
        UserScore score = new UserScore();
        for(UserAnswer userAnswer:userSolution){
            score.addScore(this.checkAnswer(userAnswer));
        }
        return score;
    }

    public Score checkAnswer(UserAnswer userAnswer) throws NoSuchElementInDatabaseException{
        Question question = this.questionRepository.findById(Long.valueOf(userAnswer.getQuestionId())).orElseThrow(NoSuchElementInDatabaseException::new);
        Score questionScore = new Score();
        questionScore.setOf(question.getPoints());
        if(question.getAnswerCorrect().equals(userAnswer.getSelectedAnswer())){
            questionScore.setPoints(question.getPoints());

        }else {
            questionScore.setPoints(0);
        }
        return questionScore;
    }
}
