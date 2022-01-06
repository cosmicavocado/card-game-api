package com.game.gameapp.service;

import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
import com.game.gameapp.model.Game;
import com.game.gameapp.model.Player;
import com.game.gameapp.repository.CardRepository;
import com.game.gameapp.repository.GameRepository;
import com.game.gameapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;

@Service
public class GameService {
    private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());
    private static List<Card> deck;
    private static Random rng = new Random();
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private CardRepository cardRepository;

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Autowired
    public void setCardRepository(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }



    //TODO Draw 10 cards
    public String drawUpToTen(Long playerId) {
        LOGGER.info("Calling drawUpToTen method from service.");
        Optional<Player> player = playerRepository.findById(playerId);
//        List<Card> tempHand = player.get().getHand();
//        ArrayList<Card> tempHand = player.get().getHand();
        System.out.println("Current player hand size " + player.get().getHand().size());
        if (player.get().getHand().size()< 10) {
            LOGGER.info("Player " + player.get().getName() + " is drawing up to 10 cards");
            do {
                // randomly "draw" from deck
                int n = rng.nextInt(deck.size());
                Card newCard = deck.get(n);
//                tempHand.add(newCard);
                // add cards from deck to hand
                player.get().setCard(newCard);
                deck.remove(n);
            } while(player.get().hand.size()<10);

                // player.get().setCard(newCard);
                // remove cards from deck

            // set hand to new hand
//            player.get().setHand();

            // remove drawn cards from deck list

            // set hand to new hand
//                player.get().setHand(player.get().getHand()); // maybe something like this??
//                playerRepository.save(player.get()); // then save??
        } else {
            LOGGER.warning("Hand is full!");
            return "Hand is full!";
        }
        System.out.println(player.get().hand.size());
        return "Cards drawn!";
    }

    public void playGame(Long playerId) {
        // get players (will turn into a loop later)
        Optional<Player> player = playerRepository.findById(playerId);
        // check all players exist
        player.get().setHand(new ArrayList<>());
        // create deck
        deck = cardRepository.findAll();
        System.out.println("initial deck size: " + cardRepository.count());
        // deal cards
        drawUpToTen(playerId);
        for (int i=0; i<player.get().hand.size(); i++) {
            System.out.println(player.get().hand.get(i).getText());
        }
        System.out.println("new size " + deck.size());
        // use rng to pick random player for judge
        // switch judges sequentially each round???
    }
}
