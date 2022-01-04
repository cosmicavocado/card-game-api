package com.game.gameapp.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cardlist")
public class CardList {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @Column
    private List<Card> deck;

    public CardList() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Card> getCardList() {
        return deck;
    }
}
