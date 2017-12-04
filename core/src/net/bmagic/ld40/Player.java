package net.bmagic.ld40;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    
    // Tweakable constants
    public static final int BASE_SPEED = 200;
    public static final float SPRITE_SCALE = 1.5f;
    public static final int HITBOX_OFFSET_X = 9;
    public static final int HITBOX_OFFSET_Y = 0;
    public static final int HITBOX_WIDTH = 12;
    public static final int HITBOX_HEIGHT = 32;
    // End tweakable constants

    // Cached instances
    private Game game;
    private Texture texture;
    private TextureRegion idleFrame;
    private Animation<TextureRegion> anim;

    // Private properties
    private Rectangle rect;
    private int bullets;
    private float stateTime;
    private boolean moving;
    private boolean right;
    private float d;

    public void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("player.png"));
        TextureRegion[] frames = TextureRegion.split(texture, 32, 32)[0];
        idleFrame = frames[0];
        anim = new Animation<TextureRegion>(0.15f, Arrays.copyOfRange(frames, 1, 3));
        anim.setPlayMode(PlayMode.LOOP_PINGPONG);
        
        rect = new Rectangle(
            game.getCameraController().getRectangle().getWidth()/2
                - idleFrame.getRegionWidth()/2 + HITBOX_OFFSET_X,
            game.getCameraController().getRectangle().getHeight()/2
                - idleFrame.getRegionHeight()/2 + HITBOX_OFFSET_Y,
            HITBOX_WIDTH,
            HITBOX_HEIGHT);

        bullets = 6;
        stateTime = 0;
        moving = false;
        right = true;
    }

    public void update() {
        moving = false;
        d = getSpeed() * Gdx.graphics.getDeltaTime();
        Rectangle backRect = game.getCameraController().getRectangle();

        // Key input
        if (Gdx.input.isKeyPressed(Keys.W)) {
            rect.y += d;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Keys.A)) {
            rect.x -= d;
            moving = true;
            right = false;
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            rect.y -= d;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            rect.x += d;
            moving = true;
            right = true;
        }

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

        // Detects collision with ammo
        List<Ammo> ammo = game.getAmmo();
        for (int i = 0; i < ammo.size(); i++)
            if (rect.overlaps(ammo.get(i).getRectangle())) {
                bullets += Ammo.CARTRIDGE_SIZE;
                ammo.remove(i);
            }

        stateTime += Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
            moving ? anim.getKeyFrame(stateTime, true) : idleFrame,
            rect.x - HITBOX_OFFSET_X, rect.y - HITBOX_OFFSET_Y,
            idleFrame.getRegionWidth()/2, idleFrame.getRegionHeight()/2,
            idleFrame.getRegionWidth(), idleFrame.getRegionHeight(),
            SPRITE_SCALE * (right ? 1 : -1), SPRITE_SCALE,
            0);
    }

    public void dispose() {
        texture.dispose();
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public int getBullets() {
        return bullets;
    }

    public void setBullets(int bullets) {
        this.bullets = bullets;
    }

    public int getSpeed() {
        return bullets > 10 ? BASE_SPEED * 10 / bullets : BASE_SPEED;
    }

    public void shoot(float angle) {
        if (bullets > 0) {
            bullets--;
            game.getBullets().add(
                new Bullet(
                    rect.x + rect.width/2,
                    rect.y + rect.height/2,
                    angle));
        }
    }

    public void die() {
        Gdx.app.log("GAMESTATE", "You lose!");
        game.setState(GameState.GAMEOVER);
    }

}