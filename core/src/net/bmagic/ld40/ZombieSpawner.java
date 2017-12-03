package net.bmagic.ld40;

import java.util.Random;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class ZombieSpawner {

    // Tweakable constants
    public static final int SPAWN_FREQUENCY = 500;
    public static final int SPAWN_SPACE_BUFFER = 20;
    // End tweakable constants

    // Cached instances
    private Game game;
    private Rectangle backgroundRect;
    private Random random;

    // Private properties
    private long lastSpawnTime;

    public void init() {
        game = Game.getInstance();
        backgroundRect = game.getCameraController().getRectangle();
        random = new Random();
        lastSpawnTime = 0;
    }

    public void update() {
        if (TimeUtils.millis() - lastSpawnTime > SPAWN_FREQUENCY) {
            int side = random.nextInt(4); // 0 is right side, goes counter-clockwise (like radians)
            int x = random.nextInt((int) backgroundRect.getWidth());
            int y = random.nextInt((int) backgroundRect.getHeight());

            switch (side) {
                case 0:
                    x = (int) backgroundRect.getWidth() - SPAWN_SPACE_BUFFER;
                    y = random.nextInt((int) backgroundRect.getHeight());
                    break;
                
                case 1:
                    x = random.nextInt((int) backgroundRect.getWidth());
                    y = (int) backgroundRect.getHeight() - SPAWN_SPACE_BUFFER;
                    break;

                case 2:
                    x = SPAWN_SPACE_BUFFER;
                    y = random.nextInt((int) backgroundRect.getHeight());
                    break;

                case 3:
                    x = random.nextInt((int) backgroundRect.getWidth());
                    y = SPAWN_SPACE_BUFFER;
                    break;

                default:
                    break;
            }

            game.getZombies().add(new Zombie(x, y));
            lastSpawnTime = TimeUtils.millis();
        }
    }
    
}