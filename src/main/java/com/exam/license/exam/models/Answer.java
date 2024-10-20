package com.exam.license.exam.models;

import jakarta.persistence.*;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    @Column(name = "answer_a")
    private String answerA;
    @Column(name = "answer_b")
    private String answerB;
    @Column(name = "answer_C")
    private String answerC;

    public Answer() {
    }

    public Answer(Long id, String answerA, String answerB, String answerC) {
        this.id = id;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }
}
