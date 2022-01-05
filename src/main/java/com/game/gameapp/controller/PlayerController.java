package com.game.gameapp.controller;

import com.game.gameapp.model.Player;
import com.game.gameapp.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path="/api")
public class PlayerController {
    private static final Logger LOGGER = Logger.getLogger(PlayerController.class.getName());
    private PlayerService playerService;

    @Autowired
    public void setPlayerService(PlayerService playerService) {this.playerService = playerService;}

    // http://localhost:9092/api/player/
    @GetMapping("/player")
    public List<Player> getPlayers() {
        LOGGER.info("Calling getPlayers method from controller");
        return playerService.getPlayers();
    }
}
