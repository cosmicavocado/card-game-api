package com.game.gameapp.controller;

import com.game.gameapp.model.Card;
import com.game.gameapp.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/")
public class GameController {
    private static final Logger LOGGER = Logger.getLogger(PlayerController.class.getName());
    private GameService gameService;

    @Autowired
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping(path="/start")
    public void startGame(LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling startGame from game controller.");
        gameService.startGame(players);
    }

    @GetMapping(path = "/status")
    public String gameStatus() {
        LOGGER.info("Calling gameStatus from game controller.");
        return gameService.gameStatus();
    }
}
