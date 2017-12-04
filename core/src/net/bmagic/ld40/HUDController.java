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

    public void init() {
        game = Game.getInstance();
        font = new BitmapFont(
            Gdx.files.internal("font.fnt"),
            Gdx.files.internal("font.png"),
            false);
    }

    public void update() {

    }

    public void draw(SpriteBatch batch) {
        font.getRegion().getTexture()
            .setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        font.getData().setScale(4);
        font.draw(batch, "Bullets:" + game.getPlayer().getBullets(), 10, Gdx.graphics.getHeight() - 10);
    }

    public void dispose() {
        font.dispose();
    }
    
}