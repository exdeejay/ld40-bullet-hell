package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

public class HUDController {

    // Tweakable constants
    public static final int SCORE_OFFSET_X = 20;
    public static final int SCORE_OFFSET_Y = 20;
    // End tweakable constants

    // Cached instances
    private Game game;
    private BitmapFont font;

    // Private properties
    private long startTime;
    private long survivedTime;
    private long survivedTimeMinutes;
    private long survivedTimeSeconds;

    public void init() {
        game = Game.getInstance();
        font = new BitmapFont(
            Gdx.files.internal("font.fnt"),
            Gdx.files.internal("font.png"),
            false);
        startTime = TimeUtils.millis();
    }

    public void update() {
        survivedTime = TimeUtils.millis() - startTime;
        survivedTimeMinutes = survivedTime / (60 * 1000);
        survivedTimeSeconds = (survivedTime % (60 * 1000)) / 1000;
    }

    public void draw(SpriteBatch batch) {
        font.getRegion().getTexture()
            .setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        font.getData().setScale(4);
        font.draw(
            batch,
            "Bullets:" + game.getPlayer().getBullets(),
            10, Gdx.graphics.getHeight() - 10);
        font.draw(
            batch,
            "Time: " + survivedTimeMinutes + ":"
                + (survivedTimeSeconds < 10 ? "0" + survivedTimeSeconds : survivedTimeSeconds),
            10, Gdx.graphics.getHeight() - 64);
    }

    public void dispose() {
        font.dispose();
    }
    
}