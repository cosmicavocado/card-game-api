package com.game.gameapp.service;

import com.game.gameapp.exception.InformationNotFoundException;
import com.game.gameapp.model.Player;
import com.game.gameapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PlayerService {
    private PlayerRepository playerRepository;
    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers(){
        LOGGER.info("calling getPlayers method from service");
        List<Player> players = playerRepository.findAll();
        if (players.isEmpty()){
            throw new InformationNotFoundException("No Players Found");
        } else {
            return players;
        }
    }
}
