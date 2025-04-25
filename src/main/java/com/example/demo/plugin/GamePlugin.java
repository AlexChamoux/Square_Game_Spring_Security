package com.example.demo.plugin;

import fr.le_campus_numerique.square_games.engine.GameFactory;

import java.util.Locale;

public interface GamePlugin {

    String getGameName(Locale locale, String defaultTypeGame);

    String getDefaultTypeGame();

    int getDefaultPlayerCount();

    int getDefaultBoardSize();

    GameFactory getGameFactory();

}
