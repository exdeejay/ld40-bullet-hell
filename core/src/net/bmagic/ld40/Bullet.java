package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {

    // Tweakable constants
    public static final int SPEED = 600;
    public static final int DESPAWN_BUFFER = 20;
    // End tweakable constants

    // Cached instances
    private static Game game;
    private static Texture texture;
    private static Rectangle backgroundRect;

    // Private properties
    private float x;
    private float y;
    private float rotation;

    public Bullet(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public static void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("bullet.png"));
        backgroundRect = game.getCameraController().getRectangle();
    }

    public void update() {
        float dPos = SPEED * Gdx.graphics.getDeltaTime();
        x += dPos * Math.cos(rotation);
        y += dPos * Math.sin(rotation);

        
        if (x + DESPAWN_BUFFER < 0
                || x - DESPAWN_BUFFER > backgroundRect.getWidth()
                || y + DESPAWN_BUFFER < 0
                || y - DESPAWN_BUFFER > backgroundRect.getHeight())
            game.getBullets().remove(this);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
            texture,
            x - texture.getWidth()/2, y - texture.getHeight()/2,
            texture.getWidth()/2, texture.getHeight()/2,
            texture.getWidth(), texture.getHeight(),
            1, 1,
            (float) Math.toDegrees(rotation),
            0, 0,
            texture.getWidth(), texture.getHeight(),
            false, false);
    }

    public static void dispose() {
        texture.dispose();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
}