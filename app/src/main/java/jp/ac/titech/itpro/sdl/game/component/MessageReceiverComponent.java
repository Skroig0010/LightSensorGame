package jp.ac.titech.itpro.sdl.game.component;

import java.util.ArrayDeque;
import java.util.Queue;

import jp.ac.titech.itpro.sdl.game.entities.Entity;
import jp.ac.titech.itpro.sdl.game.messages.Message;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public abstract class MessageReceiverComponent implements IComponent {
    public boolean alreadyNotified = false;
    private Entity parent;
    private Stage stage;
    protected Queue<Message> messageQueue = new ArrayDeque<>();

    public MessageReceiverComponent(Entity parent, Stage stage){
        this.parent = parent;
        // setNotifiedするために必要
        // Stageを渡せない状況が無いとは言い切れないのでnotifyではなくコンストラクタで値を入れておく
        this.stage = stage;
    }

    public void notify(Message message){
        this.messageQueue.add(message);
        if(!alreadyNotified){
            stage.setNotified(this);
            alreadyNotified = true;
        }
    }

    public void processMessages(){
        beforeProcess();
        for(Message msg : messageQueue){
            processMessage(msg);
        }
        afterProcess();
        alreadyNotified = false;
        messageQueue.clear();
    }

    protected abstract void processMessage(Message msg);

    protected void beforeProcess(){
    }

    protected void afterProcess(){
    }
    @Override
    public Entity getParent() {
        return parent;
    }
}
