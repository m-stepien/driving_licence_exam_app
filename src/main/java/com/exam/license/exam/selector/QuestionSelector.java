package com.exam.license.exam.selector;

import com.exam.license.exam.exceptions.NotEnoughtQuestionsException;
import com.exam.license.exam.models.Question;

import java.util.ArrayList;
import java.util.Random;

public class QuestionSelector {
    public ArrayList<Question> selectQuestionsFromSet(ArrayList<Question> questionList, int limit)
            throws NotEnoughtQuestionsException {
        if (limit > questionList.size()) {
            throw new NotEnoughtQuestionsException();
        }
        Random random = new Random();
        ArrayList<Question> questionSubset = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            int chosenIdx = random.nextInt(questionList.size());
            questionSubset.add(questionList.get(chosenIdx));
            questionList.remove(chosenIdx);
        }
        return questionSubset;
    }
}
