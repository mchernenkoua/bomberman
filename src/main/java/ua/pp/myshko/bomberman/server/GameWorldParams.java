package ua.pp.myshko.bomberman.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author M. Chernenko
 */
public class GameWorldParams {

    private final int playerDeadTime;

    private final int bombIncreasingInterval;
    private final int bombLiveTime;
    private final int bombDeadTime;
    private final int bombDamage;
    private final int initialBombCount;

    private final int fireLiveTime;

    private final int gameResponseDelay;
    private final int gameAICount;
    private final int scoreForDeadPlayer;
    private final int scoreForDeadBarrier;

    private final int dashboardVerticalSize;
    private final int dashboardHorizontalSize;
    private final int maxBarriersCountPerLine;

    public GameWorldParams() throws IOException {
        Properties properties = new Properties();
        InputStream in = getClass().getClassLoader().getResourceAsStream("gameworld.properties");
        properties.load(in);

        playerDeadTime = Integer.parseInt(properties.getProperty("playerDeadTime"));

        bombIncreasingInterval = Integer.parseInt(properties.getProperty("bombIncreasingInterval"));
        bombLiveTime = Integer.parseInt(properties.getProperty("bombLiveTime"));
        bombDeadTime = Integer.parseInt(properties.getProperty("bombDeadTime"));
        bombDamage = Integer.parseInt(properties.getProperty("bombDamage"));
        initialBombCount = Integer.parseInt(properties.getProperty("initialBombCount"));

        fireLiveTime = Integer.parseInt(properties.getProperty("fireLiveTime"));

        gameResponseDelay = Integer.parseInt(properties.getProperty("gameResponseDelay"));
        gameAICount = Integer.parseInt(properties.getProperty("gameAICount"));
        scoreForDeadPlayer = Integer.parseInt(properties.getProperty("scoreForDeadPlayer"));
        scoreForDeadBarrier = Integer.parseInt(properties.getProperty("scoreForDeadBarrier"));

        dashboardVerticalSize = Integer.parseInt(properties.getProperty("dashboardVerticalSize"));
        dashboardHorizontalSize = Integer.parseInt(properties.getProperty("dashboardHorizontalSize"));
        maxBarriersCountPerLine = Integer.parseInt(properties.getProperty("maxBarriersCountPerLine"));
    }

    public int getPlayerDeadTime() {
        return playerDeadTime;
    }

    public int getBombIncreasingInterval() {
        return bombIncreasingInterval;
    }

    public int getBombLiveTime() {
        return bombLiveTime;
    }

    public int getBombDeadTime() {
        return bombDeadTime;
    }

    public int getBombDamage() {
        return bombDamage;
    }

    public int getInitialBombCount() {
        return initialBombCount;
    }

    public int getFireLiveTime() {
        return fireLiveTime;
    }

    public int getGameResponseDelay() {
        return gameResponseDelay;
    }

    public int getGameAICount() {
        return gameAICount;
    }

    public int getScoreForDeadPlayer() {
        return scoreForDeadPlayer;
    }

    public int getScoreForDeadBarrier() {
        return scoreForDeadBarrier;
    }

    public int getMaxBarriersCountPerLine() {
        return maxBarriersCountPerLine;
    }

    public int getDashboardVerticalSize() {
        return dashboardVerticalSize;
    }

    public int getDashboardHorizontalSize() {
        return dashboardHorizontalSize;
    }
}
