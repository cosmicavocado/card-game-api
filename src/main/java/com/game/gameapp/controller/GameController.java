package com.game.gameapp.controller;

import com.game.gameapp.model.Player;
import com.game.gameapp.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api")
public class GameController {
    private static final Logger LOGGER = Logger.getLogger(PlayerController.class.getName());
    private GameService gameService;

    @GetMapping("/play")
    public void playGame(List<Player> currentPlayers) {
        LOGGER.info("Calling playGame from game logic controller.");
        // checks for minimum number of players
        if (currentPlayers.size() >= 4) {
            gameService.playGame(currentPlayers);
        }
    }
}
