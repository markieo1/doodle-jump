package com.anthony.marco.doodlelibrary.model;


import java.util.Date;

/**
 * Created by marco on 10-1-2017.
 */

public class Score {
    /**
     * The ID of the score
     */
    private long id;

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
        this(0, name, score);
    }

    public Score(String name, int score, Date date) {
        this(0, name, score, date);
    }

    public Score(long id, String name, int score) {
        this(id, name, score, new Date());
    }

    public Score(long id, String name, int score, Date date) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Trim the name to exclude all spaces that were added
        this.name = name.trim();
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
