package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class CameraController {

    // Tweakable constants
    public static final int CAMERA_WIDTH = 640;
    public static final int CAMERA_HEIGHT = 360;
    public static final int LIMIT = 40;
    // End tweakable constants

    private Game game;
    private Player player;
    private OrthographicCamera camera;

    private Rectangle rect;
    private Texture texture;
    private Vector3 playerCoords;

    public void init() {
        game = Game.getInstance();

        texture = new Texture(Gdx.files.internal("testgrid.png"));
        rect = new Rectangle(
            0,
            0,
            texture.getWidth(),
            texture.getHeight());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);
        camera.position.set(texture.getWidth()/2, texture.getHeight()/2, 0);

        player = game.getPlayer();
        playerCoords = new Vector3();
    }

    public void update() {
        playerCoords.set(
            player.getRectangle().x + player.getRectangle().width/2,
            player.getRectangle().y + player.getRectangle().height/2,
            0);
        Vector3 projected = projectToCamera(playerCoords);
        
        float d = Player.SPEED * Gdx.graphics.getDeltaTime();
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
        batch.draw(texture, rect.getX(), rect.getY());
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