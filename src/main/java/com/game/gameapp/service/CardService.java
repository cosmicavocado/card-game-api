package com.game.gameapp.service;

import com.game.gameapp.exception.InformationExistsException;
import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.CustomCard;
import com.game.gameapp.repository.CustomCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CardService {
    private static final Logger LOGGER = Logger.getLogger(CardService.class.getName());
    private CustomCardRepository customCardRepository;

    @Autowired
    public void setCustomCardRepository(CustomCardRepository customCardRepository) {
        this.customCardRepository = customCardRepository;
    }

    public CustomCard createCard(CustomCard customCardObject) {
        LOGGER.info("Calling create card method from card service.");
        CustomCard customCard = customCardRepository.findByText(customCardObject.getText());
        if (customCard != null) {
            throw new InformationExistsException("Card with text \"" + customCardObject.getText() + "\" already exists.");
        } else {
            return customCardRepository.save(customCardObject);
        }
    }

    public CustomCard updateCard(Long cardId, CustomCard customCardObject) {
        LOGGER.info("Calling updateCard method from card service.");
        Optional<CustomCard> customCard = customCardRepository.findById(cardId);
        if (customCard.isEmpty()) {
            throw new InformationNotFoundException("Card with id " + cardId + " does not exist.");
        } else {
            if (customCardObject.getText().equals(customCard.get().getText())) {
                throw new InformationExistsException("Card with text " + customCardObject.getText() + " already exists.");
            } else {
                customCard.get().setText(customCardObject.getText());
                return customCardRepository.save(customCard.get());
            }
        }
    }

    public String deleteCard(Long cardId) {
        LOGGER.info("Calling deleteCard method from card service.");
        Optional<CustomCard> card = customCardRepository.findById(cardId);
        if (card.isEmpty()) {
            throw new InformationNotFoundException("Card with id " + cardId + " does not exist.");
        } else {
            customCardRepository.deleteById(cardId);
            return "Card with id " + cardId + " deleted.";
        }
    }
}
