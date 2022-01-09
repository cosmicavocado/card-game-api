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
    private static final Random RNG = new Random();
    private static ArrayList<Card> deck;
    private static ArrayList<Prompt> prompts;
    private static ArrayList<Player> currentPlayers;
    private static LinkedHashMap<Card, Player> responses;
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
        ArrayList<Player> currentPlayers = new ArrayList<>();
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
        LOGGER.info("Calling drawUpTo10 method from game service.");
        Optional<Player> player = playerRepository.findById(playerId);
        while (player.isPresent() && player.get().getHand().size()<10) {
            int n = RNG.nextInt(deck.size());
            Card card = deck.get(n);
            player.get().setCard(card);
            deck.remove(n);
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

    public Player firstJudge(List<Player> currentPlayers) {
        int index = RNG.nextInt(currentPlayers.size());
        Player judge = currentPlayers.get(index);
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

    public Card playCard(Long playerId, int cardIndex) {
        LOGGER.info("Calling playCard method from game service.");
        // get player using id
        Player player = playerRepository.getById(playerId);
        // get player response from hand and remove it
        Card response = player.getHand().get(cardIndex);
        player.setResponse(response);
        player.hand.remove(response);
        // draw back up to 10
        drawUpToTen(playerId);
        // update responses linked hash
        responses.put(response,player);
        // return response card at given index from hand
        return response;
    }

//    public LinkedHashMap<Card,Player> getResponses(List<Player> currentPlayers, Player judge) {
//        // loop all players for this round
//        for(Player player : currentPlayers) {
//            drawUpToTen(player.getId()); // keeps all players at max hand size
//            // if player is not the judge this round
//            if(!player.equals(judge)) {
//                Card card = playCard(player.getId(),);
//                responses.put(card, player);
//                LOGGER.info(player.getName() + " played " + card.getText());
//            }
//        }
//        return responses;
//    }

    public Player getWinner(LinkedHashMap<Card,Player> responses) {
        if (responses.size() < currentPlayers.size()) {
            throw new InformationNotFoundException("Please make sure all players have responded to the prompt!");
        } else {
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

    public void playGame(LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling playGame method from game service.");
        // create deck & prompts
        deck = createDeck();
        prompts = createPrompts();

        // Saves playerIds from HashMap to ArrayList
        ArrayList<Long> playerIds = players.get("players");

        // Sets up new game & checks for valid players
        currentPlayers = newGame(playerIds);

        // use RNG to pick random first judge
        Player judge = firstJudge(currentPlayers);

        // initialize tracking variables
        int topScore = 0;
        int round = 1;

        // while topScore != 10, play game
        while(topScore != 10) {
            // draw prompt
            Prompt prompt = drawPrompt();
            LOGGER.info("Judge " + judge.getName() +" drew prompt "+ prompt.getText());
            // initialize responses to empty hashmap
            responses = new LinkedHashMap<>();
            // players play response and draw up to 10
//            LinkedHashMap<Card,Player> responses = getResponses(currentPlayers, judge);

            // WAIT FOR ALL PLAYERS TO RESPOND
            if (responses.size()< currentPlayers.size()) {

            }
            // judge chooses the best response for the round
            Player winner = getWinner(responses);

            // reset responses
            responses = new LinkedHashMap<>();

            // score tracking is updated
            topScore = checkScores(winner, topScore);

            // If game is not over
            if (topScore != 10) {
                LOGGER.info(winner.getName()+" wins round " + round + "! Their new score is " + winner.getScore());
                // rotate next judge
                judge = nextJudge(judge, currentPlayers);
                LOGGER.info("Next judge is "+ judge.getName());
            } else {
                LOGGER.info("Game Over! "+ winner.getName() + " wins!!");
            }
            LOGGER.info("End of round "+ round + ".\n");
            // increment round tracker
            round++;
        }
    }
}