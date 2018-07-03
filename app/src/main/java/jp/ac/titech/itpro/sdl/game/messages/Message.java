package jp.ac.titech.itpro.sdl.game.messages;

import java.util.ArrayList;
import java.util.List;

public class Message {
    public class BUTTON_PRESSED extends Message{
        public BUTTON_PRESSED(Object[] args){
            super(args);
        }
    }
    public class BUTTON_RELEASED extends Message{
        public BUTTON_RELEASED(Object[] args){
            super(args);
        }
    }
    public class POWER_SUPPLY extends Message{
        public POWER_SUPPLY(Object[] args){
            super(args);
        }
    }
    public class POWER_STOP extends Message{
        public POWER_STOP(Object[] args){
            super(args);
        }
    }
    private Object[] args;

    private Message(Object[] args){
        this.args = args;
    }

    public Message(){}

    public Object[] getArgs(){
        return args;
    }

    public void setArgs(Object[] args){
        this.args = args;
    }
}
