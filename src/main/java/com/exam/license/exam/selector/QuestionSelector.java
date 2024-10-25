package com.exam.license.exam.selector;

import com.exam.license.exam.models.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class QuestionSelector {
    public ArrayList<Question> selectQuestionsFromSet(ArrayList<Question> questionList, int limit){
        Random random = new Random();
        ArrayList<Question> questionSubset = new ArrayList<>();
        for(int i = 0; i < limit; i++){
            int choosenIdx = random.nextInt(questionList.size());
            questionSubset.add(questionList.get(choosenIdx));
        }
        return questionSubset;
    }
}
