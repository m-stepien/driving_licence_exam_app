package com.exam.license.exam.controlers;

import com.exam.license.exam.exceptions.EndOfQuestionsInExam;
import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.models.Score;
import com.exam.license.exam.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
    public ResponseEntity<Score> sendSolution(Map<Long, String> solution) throws NoSuchElementInDatabaseException{
        Score userScore = this.examService.checkUserSolution(solution);
        return ResponseEntity.status(200).body(userScore);
    }
}
