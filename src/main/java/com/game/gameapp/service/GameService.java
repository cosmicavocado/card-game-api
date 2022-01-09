package com.game.gameapp.service;

import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
import com.game.gameapp.model.CustomCard;
import com.game.gameapp.model.Player;
import com.game.gameapp.model.Prompt;
import com.game.gameapp.repository.CardRepository;
import com.game.gameapp.repository.CustomCardRepository;
import com.game.gameapp.repository.PlayerRepository;
import com.game.gameapp.repository.PromptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Service
public class GameService {
    private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());
    private static ArrayList<CustomCard> customCards;
    private static List<Object> deck;
    private static List<Prompt> prompts;
    private static Random rng = new Random();
    private PlayerRepository playerRepository;
    private PromptRepository promptRepository;
    private CardRepository cardRepository;
    private CustomCardRepository customCardRepository;

    @Autowired
    public void setCustomCardRepository(CustomCardRepository customCardRepository) {
        this.customCardRepository = customCardRepository;
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

    public ArrayList<Player> newGame(ArrayList<Long> playerIds) {
        LOGGER.info("Calling newGame method from game service.");
        ArrayList<Player> currentPlayers = new ArrayList<>();
        for (Long playerId : playerIds) {
            Optional<Player> player = playerRepository.findById(playerId);
            // Check that player exists
            if (player.isPresent()) {
                currentPlayers.add(player.get());
                LOGGER.info("Player " + player.get().getName() + " w/ id " + player.get().getId()
                        + " added to the game.");
                // set hand to empty
                player.get().setHand(new ArrayList<>());
                // set initial score to 0
                player.get().setScore(0);
            } else {
                throw new InformationNotFoundException("Please add only valid players and try again.");
            }
        }
        return currentPlayers;
    }

    public void drawUpToTen(Long playerId) {
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isPresent() && player.get().getHand().size()<10) {
            do {
                int n = rng.nextInt(customCards.size());
                CustomCard newCustomCard = customCards.get(n);
                player.get().setCard(newCustomCard);
                customCards.remove(n);
            } while(player.get().hand.size()<10);
        }
    }

    public List<Object> createDeck(){
        List<CustomCard> customCards = customCardRepository.findAll();
        List<Card> cards = cardRepository.findAll();
        return List.of(Stream.of(customCards, cards).toArray());
    }

    public void playGame(LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling playGame method from game service.");
        // Saves playerIds from HashMap to ArrayList
        ArrayList<Long> playerIds = players.get("players");

        // Sets up new game & checks for valid players
        ArrayList<Player> currentPlayers = newGame(playerIds);

        // create deck & prompts
        deck = createDeck();
        prompts = promptRepository.findAll();
        System.out.println("Size of deck is: "+ deck.size());
        System.out.println("Size of prompts deck is: "+ deck.size());

        // use rng to pick random player for judge
        int index = rng.nextInt(currentPlayers.size());
        Player judge = currentPlayers.get(index);
        LOGGER.info("The first judge is " + judge.getName());
        int topScore = 0;
        int round = 1;

        // while topScore != 10, loop game
        while(topScore != 10) {
            // will change if time***
            ArrayList<CustomCard> responses = new ArrayList<>();
            ArrayList<Player> responsePlayer = new ArrayList<>();

            // judge pulls prompt
            int n = rng.nextInt(prompts.size());
            Prompt prompt = prompts.get(n);
            prompts.remove(n);
            LOGGER.info(prompt.getText() + " pulled by judge " + judge.getName());

            // loop all players for this round
            for(Player player : currentPlayers) {
                drawUpToTen(player.getId()); // keeps all players at max hand size
                // if player is judge this round
                if(!player.equals(judge)) {
                    CustomCard randomCustomCard = player.hand.get(rng.nextInt(10));
                    responses.add(randomCustomCard);
                    responsePlayer.add(player);
                    LOGGER.info(player.getName() + " played " + randomCustomCard.getText());
                }
            }

            // judge picks winning response (random to simulate gameplay)
            n = rng.nextInt(responses.size());
            responses.get(n);
            Player winner = responsePlayer.get(n);

            // winning player score +1
            winner.setScore(winner.getScore()+1);
            // if player score > topScore
            if (winner.getScore() > topScore) {
                // topScore = player score
                topScore = winner.getScore();
            }
            // If game over != true
            if (topScore != 10) {
                LOGGER.info(winner.getName()+" wins round " + round + "! Their new score is " + winner.getScore());
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