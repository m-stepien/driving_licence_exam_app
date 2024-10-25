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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {

    @Mock
    QuestionRepository questionRepository;
    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    ExamService examService;

    @Test
    public void testGetQuestionsForExam(){
        Mockito.when(categoryRepository.findCategoryByName("b")).thenReturn(Optional.of(new Category(13, "b", new HashSet<Question>())));
        Assertions.assertEquals(examService.getQuestionsForExam("b"), List.of());
    }

}
