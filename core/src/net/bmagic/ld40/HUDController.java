package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.TextureData.TextureDataType;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUDController {

    // Tweakable constants
    public static final int SCORE_OFFSET_X = 20;
    public static final int SCORE_OFFSET_Y = 20;
    // End tweakable constants

    // Cached instances
    private BitmapFont font;

    // Private properties
    private int score;

    public void init() {
        font = new BitmapFont(
            Gdx.files.internal("font.fnt"),
            Gdx.files.internal("font.png"),
            false);

        score = 0;
    }

    public void update() {

    }

    public void draw(SpriteBatch batch) {
        font.getRegion().getTexture()
            .setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        font.getData().setScale(4);
        font.draw(batch, "Score:" + score, 10, Gdx.graphics.getHeight() - 10);
    }

    public void dispose() {
        font.dispose();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
}