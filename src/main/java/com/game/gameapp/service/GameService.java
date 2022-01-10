package com.game.gameapp.service;

import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
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
    private static boolean responsesReceived;
    private static LinkedHashMap<Card, Player> responses;
    private static boolean gameActive = false;
    // Repositories
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

    public ArrayList<Player> newGame(List<Long> playerIds) {
        LOGGER.info("Calling newGame method from game service.");
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
                System.out.println(player.get().getName() + " hand size: " + player.get().getHand().size());
                // set initial score to 0
                player.get().setScore(0);
            } else {
                throw new InformationNotFoundException("Please add only valid players and try again.");
            }
        }
        return currentPlayers;
    }

    public void drawUpToTen(Long playerId) {
        LOGGER.info("Calling drawUpTo10 method from game service.");
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
        LOGGER.info("Calling createDeck from game service.");
        ArrayList<Card> cards = (ArrayList<Card>) cardRepository.findAll();
//        List<CustomCard> customCards = customCardRepository.findAll();
        if (cards.isEmpty()) {
            throw new InformationNotFoundException("Please ensure the cards are loaded into the card table.");
        } else {
//            List<Object> newList = new ArrayList<>();
//            newList.add(cards);
//            newList.add(customCards);
            return cards;
        }
    }

    public ArrayList<Prompt> createPrompts() {
        LOGGER.info("Calling createPrompts from game service.");
       ArrayList<Prompt> prompts = (ArrayList<Prompt>) promptRepository.findAll();
        if (prompts.isEmpty()) {
            throw new InformationNotFoundException("Please ensure the prompts are loaded into the prompt table.");
        } else {
            return prompts;
        }
    }

    public Player firstJudge() {
        LOGGER.info("Calling first judge method from game service.");
        int index = RNG.nextInt(currentPlayers.size());
        System.out.println("Index of judge is "+index);
        judge = currentPlayers.get(index);
        LOGGER.info("The first judge is " + judge.getName());
        return judge;
    }

    public Prompt drawPrompt() {
        LOGGER.info("Calling drawPrompts from game service.");
        int n = RNG.nextInt(prompts.size());
        Prompt prompt = prompts.get(n);
        prompts.remove(n);
        return prompt;
    }

//    public synchronized void getResponses(PlayerResponses playerResponsesObject) {
//        LOGGER.info("Calling getResponses method from game service.");
//        // separate playerIds and responses
//        Long[] playerIds = playerResponsesObject.getPlayerIds();
//        Integer[] cardIndexes = playerResponsesObject.getCardIndex();
//        // iterate through each array and update the responses linked hash
//        for (int i=0; i<playerIds.length; i++) {
//            // get player
//            Player player = playerRepository.getById(playerIds[i]);
//            // get card
//            Card response = player.hand.get(cardIndexes[i]);
//            // add to responses linked hash
//            responses.put(response,player);
//            // remove from player hand
//            player.hand.remove(response);
//            // player draws back up to 10
//            drawUpToTen(playerIds[i]);
//            responsesReceived = true;
//            notifyAll();
//        }
//    }

    public Player getWinner(LinkedHashMap<Card,Player> responses) {
        // judge picks winning response (will be endpoint)
        int n = RNG.nextInt(responses.size());
        // Get keys from linkedHashMap
        List<Card> respKeyList = new ArrayList<>(responses.keySet());
        // Get winning card using index n
        Card bestResponse = respKeyList.get(n);
        Player winner = responses.get(bestResponse);
        LOGGER.info(winner.getName()+" played " + bestResponse.getText() + " this round!");
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
            LOGGER.info(winner.getName()+" wins round " + round + "! Their new score is " + winner.getScore());
            // rotate next judge
            judge = nextJudge(judge, currentPlayers);
            LOGGER.info("Next judge is "+ judge.getName());
        } else {
            LOGGER.info("Game Over! "+ winner.getName() + " wins!!");
            gameActive = false;
        }
        LOGGER.info("End of round "+ round + ".\n");
        // increment round tracker
        round++;
    }

    public void gameLoop() {
        LOGGER.info("Calling gameLoop method from game service.");
        // initialize tracking variables
        topScore = 0;
        round = 1;
        // while game != over
        while(topScore != 10) {
            // draw prompt
            Prompt prompt = drawPrompt();
            LOGGER.info("Judge " + judge.getName() +" with id " + judge.getId() + " drew prompt "+ prompt.getText());

            // reset responses to empty
            responses = new LinkedHashMap<>();

//            responsesReceived = false;

            // wait for responses
            // https://www.baeldung.com/java-wait-notify
//            while (!responsesReceived) {
//                try {
//                    wait();
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                    LOGGER.info("Error" + e + ": thread interrupted");
//                }
//            }
            // throwaway code for testing
            for(Player player : currentPlayers) {
                if(player != judge) {
                    responses.put(player.hand.get(0), player);
                    player.hand.remove(0);
                    drawUpToTen(player.getId());
                }
            }

            // judge chooses the best response for the round
            winner = getWinner(responses);

            // score tracking is updated
            topScore = checkScores(winner, topScore);

            // check for game over condition
            checkGameOver();
        }
    }

    public void startGame(LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling startGame method from game service.");
        // set game status to active
        gameActive = true;
        // create deck & prompts
        deck = createDeck();
        prompts = createPrompts();
        // Saves playerIds from HashMap to ArrayList
        ArrayList<Long> playerIds = players.get("players");
        // Sets up new game & checks for valid players
        currentPlayers = newGame(playerIds);
        // use RNG to pick random first judge
        LOGGER.info("Current players size is " + currentPlayers.size());
        judge = firstJudge();
        // while topScore != 10, play game
        gameLoop();
    }

    public String gameStatus() {
        if (!gameActive) {
            return "Game inactive. Please start a game before checking the status.";
        } else {
            StringBuilder playersToRespond = new StringBuilder();
            for (Player player : currentPlayers) {
                if (player != judge) {
                    playersToRespond.append(player.getName()).append(" id ").append(player.getId()).append(", ");
                }
            }
            return "Current judge: " + judge.getName() +
                    "\nPlayers: " + playersToRespond;
        }
    }
}