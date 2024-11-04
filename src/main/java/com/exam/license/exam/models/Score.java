package com.exam.license.exam.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int points;
    @Column(name="of_point")
    private int of;
    private long userId;
    @Column(name="time_of_exam")
    private LocalDateTime timeOfExam;

    public Score() {
        timeOfExam = LocalDateTime.now();
    }

    public Score(int points, int of) {
        this.points = points;
        this.of = of;
        timeOfExam = LocalDateTime.now();
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getTimeOfExam() {
        return timeOfExam;
    }

    public void addScore(Score score){
        this.setPoints(this.getPoints()+ score.getPoints());
        this.setOf(this.getOf() + score.getOf());
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
