package com.exam.license.exam.controlers;

import com.exam.license.exam.exceptions.EndOfQuestionsInExam;
import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Category;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.models.Score;
import com.exam.license.exam.models.User;
import com.exam.license.exam.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/{category}")
    public ResponseEntity<Map<String, Integer>> startExam(@PathVariable String category) throws NotEnoughtQuestionsException, NoSuchElementInDatabaseException {
        this.examService.createExam(category);
        Map<String, Integer> numberOfQuestionByType = this.examService.getNumberOfQuestionByType();
        ResponseEntity<Map<String, Integer>> response = ResponseEntity.ok(numberOfQuestionByType);
        return response;
    }


    @GetMapping("/question/current")
    public ResponseEntity<Question> fetchCurrentQuestion(){
        Question currentQuestion;
        ResponseEntity<Question> response;
        currentQuestion = this.examService.getCurrentQuestion();
        response = ResponseEntity.ok(currentQuestion);
        return response;
    }

    @GetMapping("/question/next")
    public ResponseEntity<Question> fetchNextQuestion(){
        Question nextQuestion;
        ResponseEntity<Question> response;
        try {
            nextQuestion = this.examService.getNextQuestion();
            response = ResponseEntity.ok(nextQuestion);
        }
        catch(EndOfQuestionsInExam e) {
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(URI.create("/exam/send/solution"));
            response = ResponseEntity.status(404).headers(headers).build();
        }
        return response;
    }

    @GetMapping("/question/number")
    public ResponseEntity<Map<String, Integer>> getNumberOfQuestion() {
        ResponseEntity<Map<String,Integer>> response;
        Map<String, Integer> questionNumber = this.examService.getNumberOfQuestionByType();
        response = ResponseEntity.ok(questionNumber);
        return response;
    }

    @PostMapping("/send/solution")
    public ResponseEntity<Void> sendSolution(@RequestBody Map<Integer, String> solution) throws NoSuchElementInDatabaseException{
        Score userScore = this.examService.checkUserSolution(solution);
        long userId = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        userScore.setUserId(userId);
        long scoreId = this.examService.saveUserScore(userScore);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/score/"+scoreId));
        return ResponseEntity.status(301).headers(headers).build();
    }

    @GetMapping("/category/all")
    public ResponseEntity<List<Category>> fetchAllCategoryAsList(){
        List<Category> categoryAll = this.examService.getAllCategoryOfQuestions();
        return ResponseEntity.ok(categoryAll);
    }
}
