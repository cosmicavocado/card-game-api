package com.game.gameapp.custom;

import com.game.gameapp.model.Card;
import com.game.gameapp.model.Player;

public class PlayerResponses {
    private Long[] playerIds;
    private Integer[] cardIndex;

    public PlayerResponses() {
    }

    public Long[] getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(Long[] playerIds) {
        this.playerIds = playerIds;
    }

    public Integer[] getCardIndex() {
        return cardIndex;
    }

    public void setCardIndex(Integer[] cardIndex) {
        this.cardIndex = cardIndex;
    }
}
