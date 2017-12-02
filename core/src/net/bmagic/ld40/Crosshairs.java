package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class Crosshairs {

    private Game game;
    private OrthographicCamera camera;
    private float x;
    private float y;
    private Texture texture;
    private Vector3 screenCoords;

    public void create() {
        game = Game.getInstance();
        camera = game.getCameraController().getCamera();
        texture = new Texture(Gdx.files.internal("crosshairs.png"));
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
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x - texture.getWidth()/2, y - texture.getHeight()/2);
    }

    public void dispose() {
        texture.dispose();
    }

}