package com.exam.license.exam.services;

import com.exam.license.exam.exceptions.EndOfQuestionsInExam;
import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.*;
import com.exam.license.exam.repository.CategoryRepository;
import com.exam.license.exam.repository.QuestionRepository;
import com.exam.license.exam.repository.ScoreRepository;
import com.exam.license.exam.utils.SolutionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.*;
@Service
@SessionScope
public class ExamService {
    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;
    private final ScoreRepository scoreRepository;
    private List<Question> exam;
    private int questionIdx;
    private Map<Integer, Integer> basicQuestionNumberByPoints = new HashMap<>();
    private Map<Integer, Integer> specializationQuestionNumberByPoints = new HashMap<>();

    @Autowired
    public ExamService(QuestionRepository questionRepository, CategoryRepository categoryRepository, ScoreRepository scoreRepository) {
        this.questionRepository = questionRepository;
        this.categoryRepository = categoryRepository;
        this.scoreRepository = scoreRepository;
        this.basicQuestionNumberByPoints.put(3, 10);
        this.basicQuestionNumberByPoints.put(2,6);
        this.basicQuestionNumberByPoints.put(1,4);
        this.specializationQuestionNumberByPoints.put(3, 6);
        this.specializationQuestionNumberByPoints.put(2, 4);
        this.specializationQuestionNumberByPoints.put(1, 2);
    }

    public void createExam(String categoryName) throws NotEnoughtQuestionsException, NoSuchElementInDatabaseException {
        categoryRepository.findCategoryByName(categoryName).orElseThrow(NoSuchElementInDatabaseException::new);
        this.questionIdx = 0;
        this.exam = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : this.basicQuestionNumberByPoints.entrySet()) {
            this.exam.addAll(fetchQuestionType(entry.getValue(), categoryName, entry.getKey(), false));
        }
        Collections.shuffle(exam);
        List<Question> specializationPart = new ArrayList<>();
        for (Map.Entry<Integer,Integer> entry : this.specializationQuestionNumberByPoints.entrySet()) {
            specializationPart.addAll(fetchQuestionType(entry.getValue(), categoryName, entry.getKey(), true));
        }
        Collections.shuffle(specializationPart);
        this.exam.addAll(specializationPart);
    }

    public Question getCurrentQuestion() {
        Question question = null;
        if (this.questionIdx < this.exam.size()) {
            question = this.exam.get(this.questionIdx);
        }
        return question;
    }

    public Question getNextQuestion() throws EndOfQuestionsInExam {
        this.questionIdx++;
        Question question = this.getCurrentQuestion();
        if (question == null) {
            throw new EndOfQuestionsInExam();
        }
        return question;
    }

    public List<Question> fetchQuestionType(int limit, String categoryName, int point, boolean isSpecialization) throws NotEnoughtQuestionsException {
        List<Question> questions;
        if (isSpecialization) {
            questions = this.questionRepository.findNSpecializationQuestionWithCategoryAndPoints(limit,
                    categoryName, point);

        } else {
            questions = this.questionRepository.findNBasicQuestionWithCategoryAndPoints(limit,
                    categoryName, point);
        }
        if (questions.size() < limit) {
            throw new NotEnoughtQuestionsException();
        }
        return questions;
    }

    public Score checkUserSolution(Map<Integer, String> userSolutionMap) throws NoSuchElementInDatabaseException {
        List<UserAnswer> userSolution = SolutionMapper.mapSolutionAnswer(userSolutionMap);
        Score userScore = new Score();
        for (UserAnswer userAnswer : userSolution) {
            userScore.addScore(this.checkAnswer(userAnswer));
        }
        return userScore;
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

    public Map<String, Integer> getNumberOfQuestionByType(){
        Map<String, Integer> questionNumberByType= new HashMap<>();
        questionNumberByType.put("basic", sumQuestionOf(this.basicQuestionNumberByPoints));
        questionNumberByType.put("specialization", sumQuestionOf(this.specializationQuestionNumberByPoints));
        return questionNumberByType;
    }

    private int sumQuestionOf(Map<Integer, Integer> questionMap){
        int sum = 0;
        for (Map.Entry<Integer,Integer> entry : questionMap.entrySet()) {
            sum+=entry.getValue();
        }
        return sum;
    }

    public List<Category> getAllCategoryOfQuestions(){
        List<Category> categoryList = this.categoryRepository.findAll();
        return categoryList;
    }

    public long saveUserScore(Score score){
        return this.scoreRepository.save(score).getId();
    }
}
