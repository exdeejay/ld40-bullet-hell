package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    
    // Tweakable constants
    public static final int SPEED = 200;
    // End tweakable constants

    // Cached instances
    private Game game;
    private Texture texture;

    // Private properties
    private Rectangle rect;

    public void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("player.png"));
        
        rect = new Rectangle(
            game.getCameraController().getRectangle().getWidth()/2 - texture.getWidth()/2,
            game.getCameraController().getRectangle().getHeight()/2 - texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight());
    }

    public void update() {
        float d = SPEED * Gdx.graphics.getDeltaTime();
        Rectangle backRect = game.getCameraController().getRectangle();

        // Key input
        if (Gdx.input.isKeyPressed(Keys.W))
            rect.y += d;
        if (Gdx.input.isKeyPressed(Keys.A))
            rect.x -= d;
        if (Gdx.input.isKeyPressed(Keys.S))
            rect.y -= d;
        if (Gdx.input.isKeyPressed(Keys.D))
            rect.x += d;

        // Limits movement to background texture
        if (rect.x < 0)
            rect.x = 0;
        if (rect.x + rect.width > backRect.getWidth())
            rect.x = backRect.getWidth() - rect.width;
        if (rect.y < 0)
            rect.y = 0;
        if (rect.y + rect.height > backRect.getHeight())
            rect.y = backRect.getHeight() - rect.height;

        // Detects collision with zombies
        for (Zombie z : game.getZombies())
            if (rect.overlaps(z.getRectangle()))
                die();
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

    public void shoot(float angle) {
        game.getBullets().add(
            new Bullet(
                rect.x + rect.width/2,
                rect.y + rect.height/2,
                angle));
    }

    public void die() {
        Gdx.app.log("GAMESTATE", "You lose!");
        game.setState(GameState.GAMEOVER);
    }

}