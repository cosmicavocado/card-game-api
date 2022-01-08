package com.game.gameapp.service;

import com.game.gameapp.model.Card;
import com.game.gameapp.model.Player;
import com.game.gameapp.model.Prompt;
import com.game.gameapp.repository.CardRepository;
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
//        LOGGER.info("Calling drawUpToTen method from game service.");
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isPresent() && player.get().getHand().size()<10) {
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
            // Check that player exists
            if (player.isPresent()) {
                currentPlayers.add(player.get());
                LOGGER.info("Player " + player.get().getName() + " w/ id " + player.get().getId() + " added to the game.");
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
        int index = rng.nextInt(currentPlayers.size());
        Player judge = currentPlayers.get(index);
        LOGGER.info("The first judge is " + judge.getName());
        int topScore = 0;
        int round = 1;

        // while topScore != 10, loop game
        while(topScore != 10) {
            // will change if time***
            ArrayList<Card> responses = new ArrayList<>();
            ArrayList<Player> responsePlayer = new ArrayList<>();

            // loop all players for this round
            for(Player player : currentPlayers) {
                drawUpToTen(player.getId()); // keeps all players at max hand size
                // if player is judge this round
                if(player.equals(judge)) {
                    int n = rng.nextInt(prompts.size());
                    Prompt prompt = prompts.get(n);
                    prompts.remove(n);
                    LOGGER.info(prompt.getText() + " pulled by judge " + judge.getName());
                } else {
                    Card randomCard = player.hand.get(rng.nextInt(10));
                    responses.add(randomCard);
                    responsePlayer.add(player);
                    LOGGER.info(player.getName() + " played " + randomCard.getText());
                }
            }

            // judge picks winning response (random to simulate gameplay)
            int n = rng.nextInt(responses.size());
            responses.get(n);
            Player winner = responsePlayer.get(n);

            // winning player score +1
            winner.setScore(winner.getScore()+1);
            LOGGER.info(winner.getName()+" wins! Their new score is " + winner.getScore());
            // if player score > topScore
            if (winner.getScore() > topScore) {
                // topScore = player score
                topScore = winner.getScore();
            }
            // If game over != true
            if (topScore != 10) {
                // Rotate next judge
                int nextJudge = index+1;
                // if index + 1 == playerIds.size()
                if (nextJudge == playerIds.size()) {
                    nextJudge = 0;
                }
                judge = playerRepository.getById(playerIds.get(nextJudge));
                LOGGER.info("Next judge is "+ judge.getName());
            } else {
                LOGGER.info("Game Over! "+ winner.getName() + " wins!!");
            }
            LOGGER.info("End of round "+ round + ".\n");
            round++;
        }
    }
}