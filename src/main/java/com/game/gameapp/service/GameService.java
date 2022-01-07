package com.game.gameapp.service;

import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
import com.game.gameapp.model.Game;
import com.game.gameapp.model.Player;
import com.game.gameapp.model.Prompt;
import com.game.gameapp.repository.CardRepository;
import com.game.gameapp.repository.GameRepository;
import com.game.gameapp.repository.PlayerRepository;
import com.game.gameapp.repository.PromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class GameService {
    private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());
    private static ArrayList<Card> deck;
    private static ArrayList<Prompt> prompts;
    private static Random rng = new Random();
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private CardRepository cardRepository;
    private PromptRepository promptRepository;

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

    @Autowired
    public void setPromptRepository(PromptRepository promptRepository) {
        this.promptRepository = promptRepository;
    }

    public String drawUpToTen(Long playerId) {
        LOGGER.info("Calling drawUpToTen method from game service.");
        Optional<Player> player = playerRepository.findById(playerId);
        System.out.println("Current player hand size " + player.get().getHand().size());
        if (player.get().getHand().size()<10) {
            LOGGER.info("Player " + player.get().getName() + " is drawing up to 10 cards");
            do {
                int n = rng.nextInt(deck.size());
                Card newCard = deck.get(n);
                player.get().setCard(newCard);
                deck.remove(n);
            } while(player.get().hand.size()<10);
        } else {
            LOGGER.warning("Hand is full!");
            return "Hand is full!";
        }
        System.out.println(player.get().hand.size());
        return "Cards drawn!";
    }

    // loop all current players in playGame
    // take an arrayList of players/playerIds
    public void playGame(HashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling playGame method from service.");
        // create deck
        deck = (ArrayList<Card>) cardRepository.findAll();
        // create prompt deck
        prompts = (ArrayList<Prompt>) promptRepository.findAll();

        // Set up players
        // for ArrayList length, loop each playerId
        for (ArrayList<Long> playerIds : players.values()) {
            for (int i=0; i<playerIds.size(); i++) {
                Optional<Player> player = playerRepository.findById(playerIds.get(i));
                System.out.println("Player " + player.get().getName()+" w/ id "+ playerIds.get(i));
                // set hand to empty
                player.get().setHand(new ArrayList<>());
                // set initial score to 0
                player.get().setScore(0);
                // deal cards
                drawUpToTen(playerIds.get(i));
            }
        }

        // while topScore != 10, loop game
        // use rng to pick random player for judge
    }
}