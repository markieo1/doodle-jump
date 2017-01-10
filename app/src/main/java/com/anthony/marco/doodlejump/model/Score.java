package com.anthony.marco.doodlejump.model;


import java.util.Date;

/**
 * Created by marco on 10-1-2017.
 */

public class Score {
    /**
     * The name of the player
     */
    private String name;

    /**
     * The achieved score
     */
    private int score;

    /**
     * The date of this result
     */
    private Date date;

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
        this.date = new Date();
    }

    public Score(String name, int score, Date date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
