package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Ammo {

    // Tweakable constants
    public static final int CARTRIDGE_SIZE = 10;
    // End tweakable constants

    // Cached instances
    private static Texture texture;

    // Private properties
    private Rectangle rect;

    public Ammo(int x, int y) {
        rect = new Rectangle(
            x - texture.getWidth()/2, y - texture.getHeight()/2,
            texture.getWidth(), texture.getHeight());
    }

    public static void init() {
        texture = new Texture(Gdx.files.internal("ammo.png"));
    }

    public void update() {

    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
    }

    public void dispose() {

    }

    public Rectangle getRectangle() {
        return rect;
    }

}