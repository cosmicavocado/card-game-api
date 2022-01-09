package com.game.gameapp.model;

import javax.persistence.*;

@Entity
@Table(name = "prompt")
public class Prompt {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_generator")
    @SequenceGenerator(name="card_generator", sequenceName = "card_seq")
    private Long id;

    @Column
    private String text;

    public Prompt() {
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
