package com.exam.license.exam.utils;

import com.exam.license.exam.models.UserAnswer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SolutionMapper {
    public static List<UserAnswer> mapSolutionAnswer(Map<Integer, String> solutionMap) {
        List<UserAnswer> userAnswers = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : solutionMap.entrySet()) {
            userAnswers.add(new UserAnswer(Math.toIntExact(entry.getKey()), entry.getValue()));
        }
        return userAnswers;
    }
}
