package com.game.gameapp.controller;

import com.game.gameapp.custom.PlayerResponses;
import com.game.gameapp.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping(path="/game/start")
    public void startGame(LinkedHashMap<String, ArrayList<Long>> players) {
        LOGGER.info("Calling startGame from game controller.");
        gameService.startGame(players);
    }

    @PostMapping(path = "/game/responses")
    public void getResponses(@RequestBody PlayerResponses playerResponsesObject) {
        LOGGER.info("Calling getResponses from game controller.");
        gameService.getResponses(playerResponsesObject);
    }
}
