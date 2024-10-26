import com.exam.license.exam.exceptions.NoSuchElementInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.*;
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

//todo 20 yn in sum
//todo 10 yn 3pkt
//todo 6 yn 2pkt
//todo 4 yn 1pkt
//todo 12 abc inside 6 3pkt 4 2pkt 2 1pkt
//todo 6 abc 3pkt
//todo 4 abc 2pkt
//todo 2 1pkt
//todo point sum 74
//todo guestion sum 32
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
        question.setPoints(1);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        Assertions.assertEquals(new Score(1,1), examService.checkAnswer(userAnswer));
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
        question.setPoints(1);
        Mockito.when(questionRepository.findById(any(Long.class))).thenReturn(Optional.of(question));
        List<UserAnswer> userSolution = new ArrayList<>();
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        userSolution.add(userAnswer);
        Assertions.assertEquals(new UserScore(1,1),examService.checkUserSolution(userSolution));
    }

    @Test
    public void testUserSolutionCheckSumTwoQuestion() throws NoSuchElementInDatabaseException {
        Question question = new Question();
        question.setId(1);
        question.setPoints(1);
        question.setAnswerCorrect("c");
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Question question2 = new Question();
        question2.setId(2);
        question2.setAnswerCorrect("b");
        question2.setPoints(1);
        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));
        List<UserAnswer> userSolution = new ArrayList<>();
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        userSolution.add(userAnswer);
        UserAnswer userAnswer2 = new UserAnswer();
        userAnswer2.setQuestionId(2);
        userAnswer2.setSelectedAnswer("b");
        userSolution.add(userAnswer2);
        Assertions.assertEquals(new UserScore(2,2),examService.checkUserSolution(userSolution));
    }

    @Test
    public void testUserSolutionCheckSumTwoQuestionOneWrong() throws NoSuchElementInDatabaseException {
        Question question = new Question();
        question.setId(1);
        question.setAnswerCorrect("c");
        question.setPoints(1);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Question question2 = new Question();
        question2.setId(2);
        question2.setPoints(1);
        question2.setAnswerCorrect("b");
        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));
        List<UserAnswer> userSolution = new ArrayList<>();
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        userSolution.add(userAnswer);
        UserAnswer userAnswer2 = new UserAnswer();
        userAnswer2.setQuestionId(2);
        userAnswer2.setSelectedAnswer("c");
        userSolution.add(userAnswer2);
        Assertions.assertEquals(new UserScore(1,2),examService.checkUserSolution(userSolution));
    }

    @Test
    public void testSumDifferentPointOfQuestion() throws NoSuchElementInDatabaseException{
        Question question = new Question();
        question.setId(1);
        question.setAnswerCorrect("c");
        question.setPoints(2);
        Mockito.when(questionRepository.findById(1L)).thenReturn(Optional.of(question));
        Question question2 = new Question();
        question2.setId(2);
        question2.setPoints(3);
        question2.setAnswerCorrect("b");
        Mockito.when(questionRepository.findById(2L)).thenReturn(Optional.of(question2));
        List<UserAnswer> userSolution = new ArrayList<>();
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(1);
        userAnswer.setSelectedAnswer("c");
        userSolution.add(userAnswer);
        UserAnswer userAnswer2 = new UserAnswer();
        userAnswer2.setQuestionId(2);
        userAnswer2.setSelectedAnswer("b");
        userSolution.add(userAnswer2);
        Assertions.assertEquals(new UserScore(5,5), this.examService.checkUserSolution(userSolution));
    }
}
