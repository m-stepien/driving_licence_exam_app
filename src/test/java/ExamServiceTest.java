import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Category;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.models.Score;
import com.exam.license.exam.models.UserAnswer;
import com.exam.license.exam.repository.CategoryRepository;
import com.exam.license.exam.repository.QuestionRepository;
import com.exam.license.exam.services.ExamService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
//TODO different point for different question


@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    QuestionRepository questionRepository;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    ExamService examService;

    @Test
    public void testGetQuestionsForCategegory() throws NoSuchElementInDatabaseException, NotEnoughtQuestionsException {
        Set<Question> questions=new HashSet<Question>();
        for(int i =0; i<14; i++){
            Question question = new Question();
            question.setId(i);
            questions.add(question);
        }
        Mockito.when(categoryRepository.findCategoryByName("b")).thenReturn(Optional.of(new Category(13, "b",questions)));
        Set<Question> subset = new HashSet<>(examService.getQuestionsForExam("b"));
        Assertions.assertEquals(subset, questions);
    }

    @Test
    public void testGetQuestionsForNotExistingCategory() throws NoSuchElementInDatabaseException, NotEnoughtQuestionsException {
        Mockito.when(categoryRepository.findCategoryByName("z")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementInDatabaseException.class, ()->examService.getQuestionsForExam("z"));
    }

    @Test
    public void testCheckTestSolution() throws NoSuchElementInDatabaseException {
        Question question = new Question();
        question.setId(1);
        question.setAnswerCorrect("c");
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        //wzorzec obserwatora do sledzenia poprawnych odpowiedzi?
        Assertions.assertTrue(examService.checkAnswer(userAnswer));
    }

    @Test
    public void testCheckTestSolutionNoQuestionWithThisId(){
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.empty());
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        Assertions.assertThrows(NoSuchElementInDatabaseException.class, ()->examService.checkAnswer(userAnswer));
    }

    @Test
    public void testUserSolutionCheck() throws NoSuchElementInDatabaseException {
        Question question = new Question();
        question.setId(1);
        question.setAnswerCorrect("c");
        Mockito.when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));
        List<UserAnswer> userSolution = new ArrayList<>();
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        userSolution.add(userAnswer);
        Assertions.assertEquals(new Score(1,1),examService.checkUserSolution(userSolution));
    }
}
