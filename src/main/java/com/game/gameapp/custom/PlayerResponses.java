package com.game.gameapp.custom;

public class PlayerResponses {
    private String[] playerNames;
    private Integer[] cardIndex;

    public PlayerResponses() {
    }

    public String[] getPlayerNames() {
        return playerNames;
    }

    public void setPlayerName(String[] playerName) {
        this.playerNames = playerName;
    }

    public Integer[] getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer[] cardIndex) {
        this.cardIndex = cardIndex;
    }
}
