package com.game.gameapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Transient
    private int score;

    @Transient
    private List<Card> hand;

    @Transient
    private Card response;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Card> getHand() {
        return hand;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public Card getResponse() {
        return response;
    }

    public void setResponse(Card response) {
        this.response = response;
    }
}
