package com.game.gameapp.service;

import com.game.gameapp.exception.InformationNotFoundException;
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
    private static final Random RNG = new Random();
    // Static Game Variables
    private static ArrayList<Card> deck;
    private static ArrayList<Prompt> prompts;
    private static ArrayList<Player> currentPlayers;
    private static Player judge;
    private static Player winner;
    private static int topScore;
    private static int round;
    private static LinkedHashMap<Card, Player> responses;
    private static boolean gameActive = false;
    // Repositories
    private PlayerRepository playerRepository;
    private PromptRepository promptRepository;
    private CardRepository cardRepository;

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

    public ArrayList<Player> newGame(List<Long> playerIds) {
        currentPlayers = new ArrayList<>();
        // loop all players
        for (Long playerId : playerIds) {
            Optional<Player> player = playerRepository.findById(playerId);
            // Check that player exists
            if (player.isPresent()) {
                currentPlayers.add(player.get());
                LOGGER.info("Player " + player.get().getName() + " w/ id " + player.get().getId()
                        + " added to the game.");
                // set hand to empty
                player.get().setHand(new ArrayList<>());
                drawUpToTen(playerId);
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
        if (player.isPresent()) {
            while (player.get().getHand().size() < 10) {
                int n = RNG.nextInt(deck.size());
                Card card = deck.get(n);
                player.get().setCard(card);
                deck.remove(n);
            }
        } else {
            throw new InformationNotFoundException("Make sure only valid players have been added to game.");
        }
    }

    public ArrayList<Card> createDeck() {
        LOGGER.info("Creating deck...");
        ArrayList<Card> cards = (ArrayList<Card>) cardRepository.findAll();
        if (cards.isEmpty()) {
            throw new InformationNotFoundException("Please ensure the cards are loaded into the card table.");
        } else {
            return cards;
        }
    }

    public ArrayList<Prompt> createPrompts() {
        LOGGER.info("Creating prompts...");
       ArrayList<Prompt> prompts = (ArrayList<Prompt>) promptRepository.findAll();
        if (prompts.isEmpty()) {
            throw new InformationNotFoundException("Please ensure the prompts are loaded into the prompt table.");
        } else {
            return prompts;
        }
    }

    public Player firstJudge() {
        int index = RNG.nextInt(currentPlayers.size());
        judge = currentPlayers.get(index);
        LOGGER.info("The first judge is " + judge.getName() + ".");
        return judge;
    }

    public void drawPrompt() {
        int n = RNG.nextInt(prompts.size());
        Prompt prompt = prompts.get(n);
        LOGGER.info("Judge " + judge.getName() + " drew " + prompt.getText() + ".");
        prompts.remove(n);
    }

    public void getResponses(List<Player> currentPlayers, Player judge) {
        // loop all players for this round
        for(Player player : currentPlayers) {
            // if player is not the judge this round
            if(!player.equals(judge)) {
                Card card = player.hand.get(RNG.nextInt(10));
                // remove card from player hand
                player.hand.remove(card);
                // add result to response map
                responses.put(card, player);
                // keeps players at max hand size
                drawUpToTen(player.getId());
                LOGGER.info(player.getName() + " played " + card.getText() + ".");
            }
        }
    }

    public Player getWinner(LinkedHashMap<Card,Player> responses) {
        // judge picks winning response
        int n = RNG.nextInt(responses.size());
        // Get keys from linkedHashMap
        List<Card> respKeyList = new ArrayList<>(responses.keySet());
        // Get winning card using index n
        Card bestResponse = respKeyList.get(n);
        winner = responses.get(bestResponse);
        return winner;
    }

    public int checkScores(Player winner, int topScore) {
        // winning player score +1
        winner.setScore(winner.getScore()+1);
        // if player score > topScore
        if (winner.getScore() > topScore) {
            // topScore = player score
            topScore = winner.getScore();
        }
        return topScore;
    }

    public Player nextJudge(Player thisJudge, ArrayList<Player> currentPlayers) {
        // find index of next judge
        int nextJudge = (currentPlayers.indexOf(thisJudge)+1);
        // if last player in list was the judge
        if (nextJudge == currentPlayers.size()) {
            // the next judge will be first player
            nextJudge = 0;
        }
        return currentPlayers.get(nextJudge);
    }

    public void checkGameOver() {
        // If game is not over
        if (topScore != 10) {
            LOGGER.info(winner.getName()+" wins round " + round + "! Their new score is " + winner.getScore() + ".");
            // rotate next judge
            judge = nextJudge(judge, currentPlayers);
            LOGGER.info("The next judge is "+ judge.getName() + ".");
        } else {
            LOGGER.info("Game Over! "+ winner.getName() + " wins!!");
            gameActive = false;
        }
        LOGGER.info("End of round "+ round + ".\n");
        // increment round tracker
        round++;
    }

    public void startGame(LinkedHashMap<String, ArrayList<Long>> players) {
        // set game status to active
        gameActive = true;
        // initialize tracking variables
        topScore = 0;
        round = 1;
        // create deck & prompts
        deck = createDeck();
        prompts = createPrompts();
        // Saves playerIds from HashMap to ArrayList
        ArrayList<Long> playerIds = players.get("players");
        // Sets up new game & checks for valid players
        currentPlayers = newGame(playerIds);
        // use RNG to pick random first judge
        judge = firstJudge();
        // while topScore != 10, play game
        while (topScore != 10) {
            playRound();
        }
    }

    public void playRound() {
        LOGGER.info("Begin round " + round + ".");
        // draw prompt
        drawPrompt();
        // reset responses to empty linked hash map
        responses = new LinkedHashMap<>();
        // simulate player responses
        getResponses(currentPlayers,judge);
        // judge chooses the best response for the round
        winner = getWinner(responses);
        // score tracking is updated
        topScore = checkScores(winner, topScore);
        // check for game over condition
        checkGameOver();
    }

    public String gameStatus() {
        LOGGER.info("Calling gameStatus from game service.");
        if (!gameActive) {
            return "Game inactive. Please start a game before checking the status.";
        } else {
            StringBuilder playersToRespond = new StringBuilder();
            for (Player player : currentPlayers) {
                if (player != judge) {
                    playersToRespond.append(player.getName()).append(" id ").append(player.getId()).append("; ");
                }
            }
            return "Current judge: " + judge.getName() + " id " + judge.getId() +
                    "\nPlayers: " + playersToRespond;
        }
    }
}