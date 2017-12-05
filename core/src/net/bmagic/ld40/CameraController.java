package net.bmagic.ld40;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class CameraController {

    // Tweakable constants
    public static final int WORLD_WIDTH = 1024;
    public static final int WORLD_HEIGHT = 1024;    
    public static final int CAMERA_WIDTH = 600;
    public static final int CAMERA_HEIGHT = 360;
    public static final int LIMIT = 200;
    // End tweakable constants

    // Cached instances
    private Game game;
    private Player player;
    private Texture texture;
    private TextureRegion tile;
    private OrthographicCamera camera;
    private Random random;

    // Private properties
    private Rectangle rect;
    private Vector3 playerCoords;
    private float shakeElapsed;
    private float shakeDuration;
    private float shakeIntensity;
    private float baseX;
    private float baseY;

    public void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("background.png"));
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        rect = new Rectangle(
            0, 0,
            WORLD_WIDTH, WORLD_HEIGHT);
        tile = new TextureRegion(texture, WORLD_WIDTH, WORLD_HEIGHT);
            
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.position.set(rect.getWidth()/2, rect.getHeight()/2, 0);
        baseX = rect.getWidth()/2;
        baseY = rect.getHeight()/2;

        random = new Random();

        player = game.getPlayer();
        playerCoords = new Vector3();

        shakeElapsed = 0;
        shakeDuration = -1;
        
        camera.update();
    }

    public void update() {
        playerCoords.set(
            player.getRectangle().x + player.getRectangle().width/2,
            player.getRectangle().y + player.getRectangle().height/2,
            0);
        Vector3 projected = projectToCamera(playerCoords);
        
        float d = game.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();

        camera.position.x = baseX;
        camera.position.y = baseY;

        if (projected.x > CAMERA_WIDTH - LIMIT)
            camera.position.x += d;
        if (projected.x < LIMIT)
            camera.position.x -= d;
        if (projected.y > CAMERA_HEIGHT - LIMIT)
            camera.position.y += d;
        if (projected.y < LIMIT)
            camera.position.y -= d;

        if (camera.position.x - CAMERA_WIDTH/2 < 0)
            camera.position.x = CAMERA_WIDTH/2;
        if (camera.position.x + CAMERA_WIDTH/2 > rect.width)
            camera.position.x = rect.width - CAMERA_WIDTH/2;
        if (camera.position.y - CAMERA_HEIGHT/2 < 0)
            camera.position.y = CAMERA_HEIGHT/2;
        if (camera.position.y + CAMERA_HEIGHT/2 > rect.height)
            camera.position.y = rect.height - CAMERA_HEIGHT/2;

        baseX = camera.position.x;
        baseY = camera.position.y;

        updateShake();

        // Don't forget to update the camera
        camera.update();
    }

    public void draw(SpriteBatch batch) {
        batch.draw(tile, rect.x, rect.y);
    }

    public void dispose() {
        texture.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public Vector3 projectToCamera(Vector3 worldCoords) {
        Vector3 projected = new Vector3();
        projected.x = worldCoords.x - (camera.position.x - CAMERA_WIDTH/2);
        projected.y = worldCoords.y - (camera.position.y - CAMERA_HEIGHT/2);
        return projected;
    }

    public Vector3 unprojectFromCamera(Vector3 screenCoords) {
        Vector3 unprojected = new Vector3();
        unprojected.x = screenCoords.x + (camera.position.x - CAMERA_WIDTH/2);
        unprojected.y = screenCoords.y + (camera.position.y - CAMERA_HEIGHT/2);
        return unprojected;
    }

    public void shake(float intensity, float duration) {
        shakeElapsed = 0;
        shakeIntensity = intensity;
        shakeDuration = duration;
    }

    private void updateShake() {
        // Only shake when required.
        if (shakeElapsed < shakeDuration) {
        
            // Calculate the amount of shake based on how long it has been shaking already
            float currentPower = shakeIntensity * ((shakeDuration - shakeElapsed) / shakeDuration);
            float x = (random.nextFloat() - 0.5f) * currentPower;
            float y = (random.nextFloat() - 0.5f) * currentPower;
            camera.translate(-x, -y);
        
            // Increase the elapsed time by the delta provided.
            shakeElapsed += Gdx.graphics.getDeltaTime();
        }
    }

}