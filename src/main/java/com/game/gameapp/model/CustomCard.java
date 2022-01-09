package com.game.gameapp.model;

import javax.persistence.*;

@Entity
@Table(name = "custom_card")
public class CustomCard {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    public CustomCard() {
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}