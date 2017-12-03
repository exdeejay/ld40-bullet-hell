package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Crosshairs {

    // Cached instances
    private Game game;
    private Texture texture;
    private OrthographicCamera camera;

    // Private properties
    private float x;
    private float y;
    private Vector3 screenCoords;

    public void init() {
        game = Game.getInstance();
        texture = new Texture(Gdx.files.internal("crosshairs.png"));
        camera = game.getCameraController().getCamera();

        screenCoords = new Vector3();
    }

    public void update() {
        screenCoords.set(
            Gdx.input.getX(),
            Gdx.input.getY(),
            0);
        Vector3 worldCoords = camera.unproject(screenCoords);
        x = worldCoords.x;
        y = worldCoords.y;

        if (Gdx.input.justTouched()) {
            float rotation = (float) Math.atan2(
                y - game.getPlayer().getRectangle().getY(),
                x - game.getPlayer().getRectangle().getX());
            game.getPlayer().shoot(rotation);
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x - texture.getWidth()/2, y - texture.getHeight()/2);
    }

    public void dispose() {
        texture.dispose();
    }

}