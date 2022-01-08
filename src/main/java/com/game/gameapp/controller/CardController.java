package com.game.gameapp.controller;

import com.game.gameapp.model.Card;
import com.game.gameapp.service.CardService;
import com.game.gameapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    // http://localhost:9092/api/card/{cardId}
    @PutMapping(path="card/{cardId}")
    public Card updateCard(@PathVariable Long cardId, @RequestBody Card cardObject) {
        LOGGER.info("Calling createCard method from card controller");
        return cardService.updateCard(cardId, cardObject);
    }

    // http://localhost:9092/api/card/{cardId}
    @DeleteMapping(path="/card/{cardId}")
    public String deleteCard(@PathVariable Long cardId) {
        LOGGER.info("Calling deleteCard method from card controller.");
        return "Card with id " + cardId + " deleted.";
    }
}
