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

    private float x;
    private float y;
    private float rotation;
    private static Texture texture;

    public Bullet(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    public static void init() {
        texture = new Texture(Gdx.files.internal("bullet.png"));
    }

    public void update() {
        float dPos = SPEED * Gdx.graphics.getDeltaTime();
        x += dPos * Math.cos(rotation);
        y += dPos * Math.sin(rotation);

        Game game = Game.getInstance();
        Rectangle backRect = game.getCameraController().getRectangle();
        if (x + DESPAWN_BUFFER < 0
                || x - DESPAWN_BUFFER > backRect.getWidth()
                || y + DESPAWN_BUFFER < 0
                || y - DESPAWN_BUFFER > backRect.getHeight())
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
    
}