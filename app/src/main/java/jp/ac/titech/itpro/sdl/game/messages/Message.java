package jp.ac.titech.itpro.sdl.game.messages;

import java.util.ArrayList;
import java.util.List;

public enum Message {
    SWITCH_PRESSED(null),
    SWITCH_RELEASED(null),
    POWER_SUPPLY(null),
    POWER_STOP(null),
    NOTHING(null);

    private Object[] args;

    private Message(Object[] args){
        this.args = args;
    }

    public Object[] getArgs(){
        return args;
    }

    public void setArgs(Object[] args){
        this.args = args;
    }
}
