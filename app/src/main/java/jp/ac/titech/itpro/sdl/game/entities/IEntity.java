package jp.ac.titech.itpro.sdl.game.entities;

import java.util.List;

import jp.ac.titech.itpro.sdl.game.Sprite;
import jp.ac.titech.itpro.sdl.game.component.IComponent;

public interface IEntity {
    void update();
    void render(Sprite sprite);
    List<IComponent> getComponents();
    <T extends IComponent> T getComponent(Class<T> tClass);
}
