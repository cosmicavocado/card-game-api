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
}
