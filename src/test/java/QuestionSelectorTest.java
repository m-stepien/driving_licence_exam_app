import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Question;
import com.exam.license.exam.selector.QuestionSelector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class QuestionSelectorTest {

    @Test
    public void testSelectFromEmpty() throws NotEnoughtQuestionsException{
        QuestionSelector selector = new QuestionSelector();
        Assertions.assertEquals(new ArrayList<Question>(), selector.selectQuestionsFromSet(new ArrayList<>(), 0));
    }

    @Test
    public void testSelectedWithLimit() throws NotEnoughtQuestionsException{
        QuestionSelector selector = new QuestionSelector();
        int limit = 2;
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        Assertions.assertEquals(2, selector.selectQuestionsFromSet(questions, limit).size());
        questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        questions.add(new Question());
        Assertions.assertEquals(2, selector.selectQuestionsFromSet(questions, limit).size());
    }

    @Test
    public void testSelectedWithLimitLargerThenSize(){
        QuestionSelector selector = new QuestionSelector();
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        int limit = questions.size()+1;

        Assertions.assertThrows(NotEnoughtQuestionsException.class, () ->
                selector.selectQuestionsFromSet(questions, limit));
    }

    @Test
    public void testSelectedIsNoDuplicate() throws NotEnoughtQuestionsException{
        QuestionSelector selector = new QuestionSelector();
        ArrayList<Question> questions = new ArrayList<>();
        for(int i=0; i < 10; i++){
            Question question = new Question();
            question.setId(i);
            questions.add(question);
        }
        ArrayList<Question> subset = selector.selectQuestionsFromSet(questions, 9);
        Set<Question> unique = new HashSet<>(subset);
        Assertions.assertEquals(unique.size(), subset.size());
    }
}
