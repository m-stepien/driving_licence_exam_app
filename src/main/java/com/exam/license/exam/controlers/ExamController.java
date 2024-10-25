package com.exam.license.exam.controlers;

import com.exam.license.exam.exceptions.NoSuchCategoryInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/exam")
public class ExamController {

    private ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/{category}")
    public List<Question> fetchQuestionList(@PathVariable String category) throws NotEnoughtQuestionsException, NoSuchCategoryInDatabaseException {
        List<Question> questionList = this.examService.getQuestionsForExam(category);
        return questionList;
    }
}
