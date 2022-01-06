package com.game.gameapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "game")
public class Game {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Transient
    private List<Player> currentPlayers;

    @Transient
    private int score;

    @Transient
    private static List<Card> deck;

    @Transient
    private List<Card> hand;

    @Transient
    private static List<Prompt> promptList;

    @Transient
    private Card response;

    public Game() {
    }

    public Game(List<Player> currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public Long getId() {
        return id;
    }

    public List<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    public void setCurrentPlayers(List<Player> currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public void setDeck(List<Card> deck) {
        Game.deck = deck;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Card getResponse() {
        return response;
    }

    public void setResponse(Card response) {
        this.response = response;
    }

    public List<Prompt> getPromptList() {
        return promptList;
    }

    public void setPromptList(List<Prompt> promptList) {
        this.promptList = promptList;
    }
}
