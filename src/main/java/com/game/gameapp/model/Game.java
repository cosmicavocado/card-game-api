package com.game.gameapp.model;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private ArrayList<Player> currentPlayers;

    @Transient
    private int topScore;

    @Transient
    private static ArrayList<Card> deck;

    @Transient
    private static ArrayList<Prompt> promptList;

    public Game() {
    }

    public Game(ArrayList<Player> currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public Long getId() {
        return id;
    }

    public ArrayList<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(ArrayList<Player> currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        Game.deck = deck;
    }

    public int getScore() {
        return topScore;
    }

    public void setScore(int topScore) {
        this.topScore = topScore;
    }

    public ArrayList<Prompt> getPromptList() {
        return promptList;
    }

    public void setPromptList(ArrayList<Prompt> promptList) {
        Game.promptList = promptList;
    }
}
