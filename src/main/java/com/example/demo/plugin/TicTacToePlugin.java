package com.example.demo.plugin;

import fr.le_campus_numerique.square_games.engine.GameFactory;
import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGameFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class TicTacToePlugin implements GamePlugin {

    private final GameFactory ticTacToeGameFactory = new TicTacToeGameFactory();

    @Getter
    @Value("${game.tictactoe.default-type-game}")
    private String defaultTypeGame;

    @Getter
    @Value("${game.tictactoe.default-player-count}")
    private int defaultPlayerCount;

    @Getter
    @Value("${game.tictactoe.default-board-size}")
    private int defaultBoardSize;

    @Autowired
    private MessageSource messageSource;

    public String getGameName(Locale locale, String defaultTypeGame) {
        return messageSource.getMessage("game." + defaultTypeGame + ".name", null, locale);
    }

    public GameFactory getGameFactory() {
        return ticTacToeGameFactory;
    }

}
