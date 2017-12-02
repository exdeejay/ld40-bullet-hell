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

    public void create() {
        game = Game.getInstance();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, CAMERA_WIDTH, CAMERA_HEIGHT);

        texture = new Texture(Gdx.files.internal("testgrid.png"));
        rect = new Rectangle(
            camera.viewportWidth/2 - texture.getWidth()/2,
            camera.viewportHeight/2 - texture.getHeight()/2,
            texture.getWidth(),
            texture.getHeight());

        player = game.getPlayer();
        playerCoords = new Vector3();
    }

    public void update() {
        playerCoords.set(
            player.getRectangle().x + player.getRectangle().width/2,
            player.getRectangle().y + player.getRectangle().height/2,
            0);
        Vector3 projected = projectToCamera(playerCoords);
        
        if (projected.x > CAMERA_WIDTH - LIMIT)
            camera.position.x += Player.SPEED * Gdx.graphics.getDeltaTime();
        if (projected.x < LIMIT)
            camera.position.x -= Player.SPEED * Gdx.graphics.getDeltaTime();
        if (projected.y > CAMERA_HEIGHT - LIMIT)
            camera.position.y += Player.SPEED * Gdx.graphics.getDeltaTime();
        if (projected.y < LIMIT)
            camera.position.y -= Player.SPEED * Gdx.graphics.getDeltaTime();

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

    public Vector3 projectToCamera(Vector3 worldCoords) {
        Vector3 projected = new Vector3();
        projected.x = worldCoords.x - (camera.position.x - CAMERA_WIDTH/2);
        projected.y = worldCoords.y - (camera.position.y - CAMERA_HEIGHT/2);
        return projected;
    }

}