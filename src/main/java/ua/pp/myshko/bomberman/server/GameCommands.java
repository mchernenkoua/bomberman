package ua.pp.myshko.bomberman.server;

/**
 * @author M. Chernenko
 */
public enum GameCommands {

    START("start"),
    QUIT("quit"),
    GETMAP("getmap"),

    ACT_LEFT("act_left"),
    ACT_RIGHT("act_right"),
    ACT_UP("act_up"),
    ACT_DOWN("act_down"),
    ACT_BOMB("act_bomb");

    private final String value;

    GameCommands(String value) {
        this.value = value;
    }

}
