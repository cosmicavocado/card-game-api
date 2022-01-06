package com.game.gameapp.service;

import com.game.gameapp.exception.InformationExistsException;
import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Card;
import com.game.gameapp.model.Player;
import com.game.gameapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;
    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

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
        if(player.isEmpty()){
            throw new InformationNotFoundException("Player with name id " + playerId + " does not exists.");
        }
        return player;
    }

    public Player createPlayer(Player playerObject) {
        LOGGER.info("Calling createPlayer method from service.");
        Player player = playerRepository.findByIdAndName(playerObject.getId(), playerObject.getName());
        if (player != null) {
            throw new InformationExistsException("Player with name " + playerObject.getName() + " already exists.");
        } else {
            return playerRepository.save(playerObject);
        }
    }

    public Player updatePlayer(Long playerId, Player playerObject) {
        LOGGER.info("Calling updatePlayer method from service.");
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
        LOGGER.info("Calling deletePlayer method from service.");
        Optional<Player> player = playerRepository.findById(playerId);
        if(player.isEmpty()){
            throw new InformationNotFoundException("Player with id " + playerId + " does not exists.");
        } else {
            return "Player with id " + playerId + " deleted.";
        }
    }
}
