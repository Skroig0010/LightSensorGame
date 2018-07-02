package jp.ac.titech.itpro.sdl.game.component;

import jp.ac.titech.itpro.sdl.game.math.Vector2;

interface ITransformable {
    void setLocal(Vector2 position);
    void setGlobal(Vector2 position);
    void setTransformParent(ITransformable parent);
    Vector2 getGlobal();
    Vector2 getLocal();
}
