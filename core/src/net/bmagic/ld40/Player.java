package net.bmagic.ld40;

import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class Player {
    
    // Tweakable constants
    public static final int BASE_SPEED = 200;
    public static final float SHOOT_VOLUME = 0.5f;
    public static final float DUD_VOLUME = 0.25f;
    public static final float PICKUP_VOLUME = 0.25f;    
    public static final float SHAKE_INTENSITY = 2f;
    public static final float SHAKE_DURATION = 0.1f;
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
    private TextureRegion arm;
    private Animation<TextureRegion> anim;
    private Sound shootSound;
    private Sound dudSound;
    private Sound pickupSound;

    // Private properties
    private Rectangle rect;
    private int bullets;
    private float stateTime;
    private Vector3 screenCoords;
    private float armRot;
    private boolean moving;
    private float d;

    public void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("player.png"));
        TextureRegion[] frames = TextureRegion.split(texture, 32, 32)[0];
        idleFrame = frames[0];
        arm = frames[4];
        anim = new Animation<TextureRegion>(0.15f, Arrays.copyOfRange(frames, 1, 3));
        anim.setPlayMode(PlayMode.LOOP_PINGPONG);
        shootSound = Gdx.audio.newSound(Gdx.files.internal("shoot.wav"));
        dudSound = Gdx.audio.newSound(Gdx.files.internal("dud.wav"));
        pickupSound = Gdx.audio.newSound(Gdx.files.internal("pickup.wav"));
        
        rect = new Rectangle(
            game.getCameraController().getRectangle().getWidth()/2
                - idleFrame.getRegionWidth()/2 + HITBOX_OFFSET_X,
            game.getCameraController().getRectangle().getHeight()/2
                - idleFrame.getRegionHeight()/2 + HITBOX_OFFSET_Y,
            HITBOX_WIDTH,
            HITBOX_HEIGHT);

        bullets = 6;
        stateTime = 0;
        screenCoords = new Vector3();
        moving = false;
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
        }
        if (Gdx.input.isKeyPressed(Keys.S)) {
            rect.y -= d;
            moving = true;
        }
        if (Gdx.input.isKeyPressed(Keys.D)) {
            rect.x += d;
            moving = true;
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
                pickupSound.play(PICKUP_VOLUME);
            }

        screenCoords.set(
            Gdx.input.getX(),
            Gdx.input.getY(),
            0);
        Vector3 worldCoords = game.getCameraController().getCamera().unproject(screenCoords);
        float x = worldCoords.x;
        float y = worldCoords.y;
        armRot = (float) Math.atan2(
            y - rect.y - HITBOX_OFFSET_Y + idleFrame.getRegionHeight()/2,
            x - rect.x - HITBOX_OFFSET_X + idleFrame.getRegionWidth()/2);
        stateTime += Gdx.graphics.getDeltaTime();
    }

    public void draw(SpriteBatch batch) {
        float adjustedArmRot;
        if (Math.toDegrees(armRot) > 90)
            adjustedArmRot = (float) Math.toDegrees(armRot) - 180;
        else if (Math.toDegrees(armRot) < -90)
            adjustedArmRot = (float) Math.toDegrees(armRot) + 180;
        else
            adjustedArmRot = (float) Math.toDegrees(armRot);

        batch.draw(
            moving ? anim.getKeyFrame(stateTime, true) : idleFrame,
            rect.x - HITBOX_OFFSET_X, rect.y - HITBOX_OFFSET_Y,
            idleFrame.getRegionWidth()/2, idleFrame.getRegionHeight()/2,
            idleFrame.getRegionWidth(), idleFrame.getRegionHeight(),
            SPRITE_SCALE
                * (Math.toDegrees(armRot) > 90 || Math.toDegrees(armRot) < -90 ? -1 : 1),
            SPRITE_SCALE,
            0);
        batch.draw(
            arm,
            rect.x - HITBOX_OFFSET_X, rect.y - HITBOX_OFFSET_Y,
            idleFrame.getRegionWidth()/2, idleFrame.getRegionHeight()/2,
            idleFrame.getRegionWidth(), idleFrame.getRegionHeight(),
            SPRITE_SCALE 
                * (Math.toDegrees(armRot) > 90 || Math.toDegrees(armRot) < -90 ? -1 : 1),
            SPRITE_SCALE,
            adjustedArmRot);
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
            shootSound.play(SHOOT_VOLUME);
            game.getCameraController().shake(SHAKE_INTENSITY, SHAKE_DURATION);
        } else {
            dudSound.play(DUD_VOLUME);
        }
    }

    public void die() {
        Gdx.app.log("GAMESTATE", "You lose!");
        game.setState(GameState.GAMEOVER);
    }

}