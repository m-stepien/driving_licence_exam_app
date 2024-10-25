import com.exam.license.exam.models.Question;
import com.exam.license.exam.selector.QuestionSelector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
//TODO
//testSelectedWithLimitLargerThenSize should throw some exception
//should not be any duplicate of question

public class QuestionSelectorTest {
    @Test
    public void testSelectFromEmpty(){
        QuestionSelector selector = new QuestionSelector();
        Assertions.assertEquals(new ArrayList<Question>(), selector.selectQuestionsFromSet(new ArrayList<>(), 0));
    }

    @Test
    public void testSelectedWithLimit(){
        QuestionSelector selector = new QuestionSelector();
        int limit = 2;
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question());
        Assertions.assertEquals(2, selector.selectQuestionsFromSet(questions, limit).size());
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
        Assertions.assertEquals(limit, selector.selectQuestionsFromSet(questions, limit).size());
    }
}
