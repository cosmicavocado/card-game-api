package com.game.gameapp.service;

import com.game.gameapp.exception.InformationExistsException;
import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
import com.game.gameapp.model.Player;
import com.game.gameapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class PlayerService {
    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());
    private PlayerRepository playerRepository;
    private GameService gameService;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers() {
        LOGGER.info("Calling getPlayers method from service.");
        List<Player> players = playerRepository.findAll();
        if (players.isEmpty()) {
            throw new InformationNotFoundException("No players found.");
        } else {
            return players;
        }
    }

    public Optional<Player> getPlayer(Long playerId) {
        LOGGER.info("Calling getPlayer method from service.");
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isEmpty()) {
            throw new InformationNotFoundException("Player with name id " + playerId + " does not exists.");
        }
        return player;
    }

    public Player createPlayer(Player playerObject) {
        LOGGER.info("Calling createPlayer method from player service.");
        Player player = playerRepository.findByName(playerObject.getName());
        if (player != null) {
            throw new InformationExistsException("Player with name " + playerObject.getName() + " already exists.");
        } else {
            return playerRepository.save(playerObject);
        }
    }

    public Player updatePlayer(Long playerId, Player playerObject) {
        LOGGER.info("Calling updatePlayer method from player service.");
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isPresent()) {
            if (playerObject.getName().equals(player.get().getName())) {
                throw new InformationExistsException("Player with name " + playerObject.getName() + " already exists.");
            } else {
                player.get().setName(playerObject.getName());
                return playerRepository.save(player.get());
            }
        } else {
            throw new InformationNotFoundException("Player with name " + playerObject.getName() + " does not exists.");
        }
    }

    public String deletePlayer(Long playerId) {
        LOGGER.info("Calling deletePlayer method from player service.");
        Optional<Player> player = playerRepository.findById(playerId);
        if (player.isEmpty()) {
            throw new InformationNotFoundException("Player with id " + playerId + " does not exists.");
        } else {
            playerRepository.deleteById(playerId);
            return "Player with id " + playerId + " deleted.";
        }
    }

    public void drawUpToTen(Long playerId){
        gameService.drawUpToTen(playerId);
    }

    public void startGame(LinkedHashMap<String, ArrayList<Long>> players) {
        gameService.startGame(players);
    }

    public Card playCard(Long playerId, int cardIndex) {
        return gameService.playCard(playerId,cardIndex);
    }
}
