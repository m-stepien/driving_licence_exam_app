package com.exam.license.exam.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String question;
    @Column(name = "answer_correct")
    private String answerCorrect;
    @OneToOne
    @JoinColumn(name = "answers_id")
    private Answer answer;
    @OneToOne
    @JoinColumn(name = "media_id")
    private Media media;
    @Column(name = "points")
    private int points;
    public Question() {
    }

    public Question(Long id, String question, String answerCorrect, Answer answer, Media media, int points) {
        this.id = id;
        this.question = question;
        this.answerCorrect = answerCorrect;
        this.answer = answer;
        this.media = media;
        this.points = points;
    }

    public Long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = Long.valueOf(id);
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswerCorrect() {
        return answerCorrect;
    }

    public void setAnswerCorrect(String answerCorrect) {
        this.answerCorrect = answerCorrect;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
