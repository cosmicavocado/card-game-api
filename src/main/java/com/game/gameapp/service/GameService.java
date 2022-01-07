package com.game.gameapp.service;

import com.game.gameapp.model.Card;
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
        return "Cards drawn!";
    }

    // loop all current players in playGame
    // take an arrayList of players/playerIds
    public void playGame(LinkedHashMap<String, ArrayList<Long>> players) {
        ArrayList<Long>playerIds = players.get("players");
        LOGGER.info("Calling playGame method from service.");
        // create deck
        deck = (ArrayList<Card>) cardRepository.findAll();
        // create prompt deck
        prompts = (ArrayList<Prompt>) promptRepository.findAll();

        // Set up players
        // for ArrayList length, loop each playerId
        for (ArrayList<Long> playerIdsList : players.values()) {
            for (int i=0; i<playerIdsList.size(); i++) {
                Optional<Player> player = playerRepository.findById(playerIdsList.get(i));
                LOGGER.info("Player " + player.get().getName()+" w/ id "+ playerIdsList.get(i));
                // set hand to empty
                player.get().setHand(new ArrayList<>());
                // set initial score to 0
                player.get().setScore(0);
                // deal cards
                drawUpToTen(playerIdsList.get(i));
            }
        }

        // use rng to pick random player for judge
        int index = rng.nextInt(players.values().size());
        Optional<Player> judge = playerRepository.findById(playerIds.get(index));
        LOGGER.info("The first judge is " + judge.get().getName());
        int topScore = 0;

        // while topScore != 10, loop game
        while (topScore != 10) {
            // judge draws a prompt
            int rand = rng.nextInt(prompts.size());
            Prompt p = prompts.get(rand);
            prompts.remove(rand);

            ArrayList<Card> responses = new ArrayList<Card>();

            // all non judge players give response
            for(int i=0; i<playerIds.size(); i++) {
                if (i != judge.get().getId()) {
                    // get non judge player
                    Optional<Player> currPlayer = playerRepository.findById(playerIds.get(i));
                    // random response card to simulate player choice
                    Card randomCard = currPlayer.get().hand.get(rng.nextInt(10));
                    responses.add(randomCard);
                    LOGGER.info(currPlayer.get().getName() + " played " + randomCard.getText());
                }
            }
            
            // judge picks winning response
            // winning player score +1
            // if player score > topScore
                // topScore = player score
            // if index + 1 == playerIds.size()
                // index resets to 0
            // assign next judge at index + 1
            topScore = 10;
        }
    }
}