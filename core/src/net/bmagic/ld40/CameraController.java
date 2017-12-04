package net.bmagic.ld40;

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
    public static final int CAMERA_WIDTH = 800;
    public static final int CAMERA_HEIGHT = 640;
    public static final int LIMIT = 200;
    // End tweakable constants

    // Cached instances
    private Game game;
    private Player player;
    private Texture texture;
    private TextureRegion tile;
    private OrthographicCamera camera;

    // Private properties
    private Rectangle rect;
    private Vector3 playerCoords;

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

        player = game.getPlayer();
        playerCoords = new Vector3();
    }

    public void update() {
        playerCoords.set(
            player.getRectangle().x + player.getRectangle().width/2,
            player.getRectangle().y + player.getRectangle().height/2,
            0);
        Vector3 projected = projectToCamera(playerCoords);
        
        float d = game.getPlayer().getSpeed() * Gdx.graphics.getDeltaTime();
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

}