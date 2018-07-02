package jp.ac.titech.itpro.sdl.game.graphics.animation;

import java.util.HashMap;
import java.util.Map;

import jp.ac.titech.itpro.sdl.game.component.SpriteComponent;

public class AnimationController {

    // 1アニメーション(歩行とか点滅とか)のデータ定義
    public class AnimationData{
        public final int start;      // アニメーション開始画像番号
        public final int end;        // アニメーション終了画像番頭
        public final int interval;   // ミリ秒単位
        public final boolean isLoop; // ループするか否か

        public AnimationData(final int start, final int end, final int interval, final boolean isLoop){
            this.start = start;
            this.end = end;
            this.isLoop = isLoop;
            this.interval = interval;
        }

        public AnimationData(final int frameID){
            this(frameID, frameID, 0, false);
        }

    }

    private Map<String, AnimationData> animations = new HashMap<>();

    private SpriteComponent sprite;

    public AnimationController(SpriteComponent sprite){
        this.sprite = sprite;
        currAnim = new AnimationData(0, 0, 0, false);
        animations.put("default", currAnim);
        currAnimName = "default";
    }

    private long animStartTime;
    private AnimationData currAnim;
    private String currAnimName;

    public void addAnimation(AnimationData data, String name){
        animations.put(name, data);
    }

    public void setCurrentAnimation(String name){
        if(!currAnimName.equals(name)) {
            currAnimName = name;
            animStartTime = System.nanoTime();
            currAnim = animations.get(name);
        }
    }

    public void resetAnimationTime(){
        animStartTime = System.nanoTime();
    }

    public void update(){
        if(currAnim.start != currAnim.end) {
            long currTime = System.nanoTime();
            int elapsedTime = (int)((currTime - animStartTime) / 1000000 / currAnim.interval);
            int currAnimNumber;
            int numOfFrames = (currAnim.end - currAnim.start + 1);
            if(currAnim.isLoop) {
                currAnimNumber = currAnim.start + elapsedTime % numOfFrames;
            }else{
                currAnimNumber = currAnim.start + Math.min(elapsedTime, numOfFrames);
            }

            // テクスチャの横のチップ数
            int chipNum = sprite.getTexture().width / sprite.rect.width;
            sprite.rect.x = sprite.rect.width * (currAnimNumber % chipNum);
            sprite.rect.y = sprite.rect.height * (currAnimNumber / chipNum);
        }else{
            // テクスチャの横のチップ数
            int chipNum = sprite.getTexture().width / sprite.rect.width;
            sprite.rect.x = sprite.rect.width * (currAnim.start % chipNum);
            sprite.rect.y = sprite.rect.height * (currAnim.start / chipNum);
        }
    }
}
