package com.exam.license.exam.models;

public class UserScore extends Score{
    public UserScore() {
        super(0,0);
    }

    public UserScore(int points, int of) {
        super(points, of);
    }
    public void addScore(){
        this.points+=1;
        this.of+=1;
    }

    public void addScore(Score score){
        this.points+=score.getPoints();
        this.of+=score.getOf();
    }
}
