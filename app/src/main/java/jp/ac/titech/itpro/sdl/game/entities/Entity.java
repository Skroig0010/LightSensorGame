package jp.ac.titech.itpro.sdl.game.entities;

import java.util.ArrayList;
import java.util.List;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.IComponent;
import jp.ac.titech.itpro.sdl.game.stage.Stage;

public abstract class Entity {
    private List<IComponent> components = new ArrayList<IComponent>();
    private Stage stage;
    public Entity(Stage stage){
        this.stage = stage;
    }
    public void addComponent(IComponent component){
        components.add(component);
        stage.addComponent(component);
    }
    public void removeComponent(IComponent component){
        if(components.contains(component)) {
            components.remove(component);
            stage.removeComponent(component);
        }
    }
    public List<IComponent> getComponents() {
        return components;
    }

    public <T extends IComponent> T getComponent(Class<?> tClass) {
        for(IComponent component : components){
            if(tClass.isInstance(component)){
                return (T)component;
            }
        }
        return null;
    }
}
