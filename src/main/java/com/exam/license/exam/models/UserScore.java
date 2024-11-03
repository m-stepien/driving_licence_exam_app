package com.exam.license.exam.models;

public class UserScore extends Score{
    public UserScore() {
        super(0,0);
    }

    public UserScore(int points, int of) {
        super(points, of);
    }

    public void addScore(Score score){
        this.setPoints(this.getPoints()+ score.getPoints());
        this.setOf(this.getOf() + score.getOf());
    }
}
