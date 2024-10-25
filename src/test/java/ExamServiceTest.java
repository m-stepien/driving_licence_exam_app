import com.exam.license.exam.exceptions.NoSuchCategoryInDatabaseException;
import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Category;
import com.exam.license.exam.models.Question;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

//TODO
//check solution of test
//handling situation when solution has got less answer then number of question
//score is correct


@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    QuestionRepository questionRepository;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    ExamService examService;

    @Test
    public void testGetQuestionsForCategegory() throws NoSuchCategoryInDatabaseException, NotEnoughtQuestionsException {
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
    public void testGetQuestionsForNotExistingCategory() throws NoSuchCategoryInDatabaseException, NotEnoughtQuestionsException {
        Mockito.when(categoryRepository.findCategoryByName("z")).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchCategoryInDatabaseException.class, ()->examService.getQuestionsForExam("z"));
    }

    @Test
    public void testCheckTestSolution(){

    }

}
