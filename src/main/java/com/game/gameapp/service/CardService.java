package com.game.gameapp.service;

import com.game.gameapp.exception.InformationExistsException;
import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
import com.game.gameapp.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CardService {
    private static final Logger LOGGER = Logger.getLogger(CardService.class.getName());
    private CardRepository cardRepository;

    @Autowired
    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    //TODO does not generate unique ID when database already has cards??
    public Card createCard(Card cardObject) {
        LOGGER.info("Calling create card method from card service.");
        Card card = cardRepository.findByText(cardObject.getText());
        if (card != null) {
            throw new InformationExistsException("Card with text \"" + cardObject.getText() + "\" already exists.");
        } else {
            return cardRepository.save(cardObject);
        }
    }

    public Card updateCard(Long cardId, Card cardObject) {
        LOGGER.info("Calling updateCard method from card service.");
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isPresent()) {
            if (cardObject.getText().equals(card.get().getText())) {
                throw new InformationExistsException("Card with text " + cardObject.getText() + " already exists.");
            } else {
                card.get().setText(cardObject.getText());
                return cardRepository.save(card.get());
            }
        } else {
            throw new InformationNotFoundException("Card with id " + cardObject.getId() + " does not exist.");
        }
    }

    public String deleteCard(Long cardId, Card cardObject) {
        Optional<Card> card = cardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new InformationNotFoundException("Card with id " + cardObject.getId() + " does not exist.");
        } else {
            cardRepository.deleteById(cardId);
            return "Card with id " + cardId + " deleted.";
        }
    }
}
