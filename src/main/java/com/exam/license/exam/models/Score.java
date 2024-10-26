package com.exam.license.exam.models;

import java.util.Objects;

public class Score {
    int points;
    int of;

    public Score() {
    }

    public Score(int points, int of) {
        this.points = points;
        this.of = of;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getOf() {
        return of;
    }

    public void setOf(int of) {
        this.of = of;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Score score = (Score) o;
        return points == score.points && of == score.of;
    }

    @Override
    public int hashCode() {
        return Objects.hash(points, of);
    }

    @Override
    public String toString(){
        return "point: " +this.getPoints() +"\nof:"+this.getOf();
    }
}
