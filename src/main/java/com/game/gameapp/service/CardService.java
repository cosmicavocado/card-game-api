package com.game.gameapp.service;

import com.game.gameapp.exception.InformationExistsException;
import com.game.gameapp.model.Card;
import com.game.gameapp.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.logging.Logger;

@Service
public class CardService {
    private static final Logger LOGGER = Logger.getLogger(CardService.class.getName());
    private CardRepository cardRepository;

    @Autowired
    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    //TODO does not generate uniqque ID when database already has cards??
    public Card createCard(Card cardObject) {
        LOGGER.info("Calling create card method from card service.");
        Card card = cardRepository.findByText(cardObject.getText());
        if (card != null) {
            throw new InformationExistsException("Card with text \"" + cardObject.getText() + "\" already exists.");
        } else {
            return cardRepository.save(cardObject);
        }
    }
}
