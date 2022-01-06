package com.game.gameapp.service;

import com.game.gameapp.model.Player;
import com.game.gameapp.repository.GameRepository;
import com.game.gameapp.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class GameService {
    private GameRepository gameRepository;
    private PlayerRepository playerRepository;
    private static final Logger LOGGER = Logger.getLogger(GameService.class.getName());

    @Autowired
    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Autowired
    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void playGame(List<Player> currentPlayers) {
        // create deck
        // deal cards
        // randomly choose first round judge
        // switch judges every round
    }
}
