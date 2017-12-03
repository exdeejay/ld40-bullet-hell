package net.bmagic.ld40;

import java.awt.Rectangle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Zombie {

    // Cached instances
    private static Game game;
    private static Texture texture;

    // Private properties
    private Rectangle rect;

    public Zombie(int x, int y) {
        rect = new Rectangle(x, y, texture.getWidth(), texture.getHeight());
    }

    public static void create() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("zombie.png"));
    }

    public void update() {

    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
    }

    public void dispose() {
        texture.dispose();
    }

}