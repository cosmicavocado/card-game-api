package com.game.gameapp.model;

import javax.persistence.*;
import java.util.ArrayList;

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
    public ArrayList<CustomCard> hand;

    @Transient
    private int score;

    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<CustomCard> getHand() {
        return hand;
    }

    public void setHand(ArrayList<CustomCard> hand) {
        this.hand = hand;
    }

    public void setCard(CustomCard customCard) {
        this.hand.add(customCard);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
