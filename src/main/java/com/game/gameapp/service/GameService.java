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
    private PlayerRepository playerRepository;
    private CardRepository cardRepository;
    private PromptRepository promptRepository;

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

    public void drawUpToTen(Long playerId) {
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
        }
    }

    // loop all current players in playGame
    // take an arrayList of players/playerIds
    public void playGame(LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling playGame method from service.");
        // Empty list to store player objects
        ArrayList<Player> currentPlayers = new ArrayList<>();
        // Saves playerIds from HashMap to ArrayList
        ArrayList<Long> playerIds = players.get("players");

        // For all playerIds
        for (Long playerId : playerIds) {
            Optional<Player> player = playerRepository.findById(playerId);
            if (player.isPresent()) {
                currentPlayers.add(player.get());
                LOGGER.info("Player " + player.get().getName() + " w/ id " + player.get().getName() + " added to the game.");
                // set hand to empty
                player.get().setHand(new ArrayList<>());
                // set initial score to 0
                player.get().setScore(0);
            }
        }
        // create deck & prompts
        deck = (ArrayList<Card>) cardRepository.findAll();
        prompts = (ArrayList<Prompt>) promptRepository.findAll();

        // use rng to pick random player for judge
        int index = rng.nextInt(players.values().size());
        Optional<Player> judge = playerRepository.findById(playerIds.get(index));
        LOGGER.info("The first judge is " + judge.get().getName());
        int topScore = 0;

        // while topScore != 10, loop game
        while (topScore != 10) {
            // deal cards


            // judge draws a prompt
            int rand = rng.nextInt(prompts.size());
            Prompt p = prompts.get(rand);
            prompts.remove(rand);

            ArrayList<Card> responses = new ArrayList<>();
            ArrayList<Player> responsePlayer = new ArrayList<>();
            // all non judge players give response
            //TODO FIX TO EXCLUDE JUDGE
            for(int i=0; i<playerIds.size(); i++) {
                drawUpToTen(playerIds.get(i));
                // get non judge player
                Optional<Player> currPlayer = playerRepository.findById(playerIds.get(i));
                if (i != index) {
                    // random response card to simulate player choice
                    Card randomCard = currPlayer.get().hand.get(rng.nextInt(10));
                    responses.add(randomCard);
                    responsePlayer.add(currPlayer.get());
                    LOGGER.info(currPlayer.get().getName() + " played " + randomCard.getText());
                }
            }

            // judge picks winning response (random to simulate gameplay)
            int resp = rng.nextInt(responses.size());
            responses.get(resp);
            Player winner = responsePlayer.get(resp);

            // winning player score +1
            winner.setScore(winner.getScore()+1);
            LOGGER.info(winner.getName()+" new score is " + winner.getScore());
            // if player score > topScore
            if (winner.getScore() > topScore) {
                // topScore = player score
                topScore = winner.getScore();
            }
            // Check for gameover
            if (topScore != 10) {
                // Rotate next judge
                int nextJudge = index+1;
                // if index + 1 == playerIds.size()
                if (nextJudge == playerIds.size()) {
                    nextJudge = 0;
                }
                judge = playerRepository.findById(playerIds.get(nextJudge));
                LOGGER.info("Next judge is "+ judge.get().getName());
            } else {
                LOGGER.info("Game Over! "+ winner.getName() + " wins!!");
            }
        }
    }
}