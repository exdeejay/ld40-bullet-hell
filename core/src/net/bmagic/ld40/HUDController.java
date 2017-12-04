package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUDController {

    // Tweakable constants
    public static final int SCORE_OFFSET_X = 20;
    public static final int SCORE_OFFSET_Y = 20;
    // End tweakable constants

    // Cached instances
    private Game game;
    private BitmapFont font;

    // Private properties
    private int survivedTimeMinutes;
    private float survivedTimeSeconds;
    private int score;

    public void init() {
        game = Game.getInstance();
        font = game.getFont();
        survivedTimeSeconds = 0;
        score = 0;
    }

    public void update() {
        survivedTimeSeconds += Gdx.graphics.getDeltaTime();
        survivedTimeMinutes = (int) (survivedTimeSeconds / 60);
    }

    public void draw(SpriteBatch batch) {
        font.getRegion().getTexture()
            .setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        font.getData().setScale(4);
        font.draw(
            batch,
            "Bullets:" + game.getPlayer().getBullets(),
            10, Gdx.graphics.getHeight() - 10);
        font.draw(batch, "Kills:" + score, 10, Gdx.graphics.getHeight() - 64);
        font.draw(
            batch,
            "Time: " + survivedTimeMinutes + ":"
                + (survivedTimeSeconds < 10 ? "0" + (int) survivedTimeSeconds : (int) survivedTimeSeconds),
            10, 48);
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

    public void incrScore() {
        score++;
    }
    
}