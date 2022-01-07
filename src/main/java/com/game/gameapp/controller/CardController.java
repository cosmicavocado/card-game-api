package com.game.gameapp.controller;

import com.game.gameapp.model.Card;
import com.game.gameapp.service.CardService;
import com.game.gameapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api")
public class CardController {
    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());
    private CardService cardService;

    @Autowired
    public void setCardService(CardService cardService) {
        this.cardService = cardService;
    }

    // http://localhost:9092/api/card
    @PostMapping(path="/card")
    public Card createCard(@RequestBody Card cardObject) {
        LOGGER.info("Calling createCard method from card controller");
        return cardService.createCard(cardObject);
    }
}
