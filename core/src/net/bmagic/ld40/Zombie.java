package net.bmagic.ld40;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Zombie {

    // Tweakable constants
    public static final int SPEED = 80;
    // End tweakable constants

    // Cached instances
    private static Game game;
    private static Texture texture;
    
    // Private properties
    private Rectangle rect;
    private Vector2 path;

    public Zombie(int x, int y) {
        rect = new Rectangle(
            x - texture.getWidth()/2,
            y - texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight());
        path = new Vector2();
    }

    public static void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("zombie.png"));
    }

    public void update() {
        Rectangle playerRect = game.getPlayer().getRectangle();
        path.set(playerRect.x - rect.x, playerRect.y - rect.y);
        path = path.limit(SPEED * Gdx.graphics.getDeltaTime());

        rect.x += path.x;
        rect.y += path.y;

        // Detect collision with bullet
        List<Bullet> bullets = game.getBullets();
        for (int i = 0; i < bullets.size(); i++)
            if (rect.contains(bullets.get(i).getX(), bullets.get(i).getY())) {
                game.getBullets().remove(i);
                die();
            }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, rect.x, rect.y);
    }

    public static void dispose() {
        texture.dispose();
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public void die() {
        game.getZombies().remove(this);
    }

}