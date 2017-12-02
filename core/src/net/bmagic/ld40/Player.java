package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    
    // Tweakable constants
    public static final int SPEED = 200;
    // End tweakable constants

    private Game game;
    private OrthographicCamera camera;
    private Rectangle rect;
    private Texture texture;

    public void create() {
        game = Game.getInstance();
        camera = game.getCameraController().getCamera();
        texture = new Texture(Gdx.files.internal("player.png"));
        rect = new Rectangle(
            camera.viewportWidth/2 - texture.getWidth()/2,
            camera.viewportHeight/2 - texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight());
    }

    public void update() {
        if (Gdx.input.isKeyPressed(Keys.W))
            rect.y += SPEED * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.A))
            rect.x -= SPEED * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.S))
            rect.y -= SPEED * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.D))
            rect.x += SPEED * Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, rect.getX(), rect.getY());
    }

    public void dispose() {
        texture.dispose();
    }

    public Rectangle getRectangle() {
        return rect;
    }

}