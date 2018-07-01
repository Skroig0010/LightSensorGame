package jp.ac.titech.itpro.sdl.game.entities;

import java.util.ArrayList;
import java.util.List;

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
    public <T extends IComponent> List<T>  getComponents(String typeName) {
        List<T> lst = new ArrayList<T>();
        try {
            Class<?> cls = Class.forName(typeName);
            for(IComponent component : components){
                if(cls.isInstance(component)){
                    lst.add((T)component);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return lst;
    }

    public <T extends IComponent> T getComponent(String typeName) {
        try {
            Class<?> cls = Class.forName(typeName);
            for(IComponent component : components){
                if(cls.isInstance(component)){
                    return (T)component;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
