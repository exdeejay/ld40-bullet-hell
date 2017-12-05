package net.bmagic.ld40;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class HUDController {
    
    // Cached instances
    private Game game;
    private OrthographicCamera camera;
    private BitmapFont font;

    // Private properties
    private Vector3 bulletsScreenCoords;
    private Vector3 bulletsWorldCoords;
    private Vector3 scoreScreenCoords;
    private Vector3 scoreWorldCoords;
    private Vector3 timeScreenCoords;
    private Vector3 timeWorldCoords;
    private int survivedTimeMinutes;
    private float survivedTimeSeconds;
    private int score;

    public void init() {
        game = Game.getInstance();
        camera = game.getCameraController().getCamera();
        font = game.getFont();
        bulletsScreenCoords = new Vector3();
        scoreScreenCoords = new Vector3();
        timeScreenCoords = new Vector3();
        survivedTimeSeconds = 0;
        score = 0;
    }

    public void update() {
        survivedTimeSeconds += Gdx.graphics.getDeltaTime();
        survivedTimeMinutes = (int) (survivedTimeSeconds / 60);
    }

    public void draw(SpriteBatch batch) {
        font.getRegion().getTexture()
            .setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        font.getData().setScale(2);
        bulletsScreenCoords.set(10, camera.viewportHeight - 10, 0);
        bulletsWorldCoords = game.getCameraController().unprojectFromCamera(bulletsScreenCoords);
        font.draw(
            batch,
            "Bullets:" + game.getPlayer().getBullets(),
            bulletsWorldCoords.x, bulletsWorldCoords.y);
            
        scoreScreenCoords.set(10, camera.viewportHeight - 40, 0);
        scoreWorldCoords = game.getCameraController().unprojectFromCamera(scoreScreenCoords);
        font.draw(
            batch,
            "Kills:" + score,
            scoreWorldCoords.x, scoreWorldCoords.y);

        timeScreenCoords.set(10, 32, 0);
        timeWorldCoords = game.getCameraController().unprojectFromCamera(timeScreenCoords);
        font.draw(
            batch,
            "Time: " + survivedTimeMinutes + ":"
                + (survivedTimeSeconds < 10 ? "0" + (int) survivedTimeSeconds : (int) survivedTimeSeconds),
            timeWorldCoords.x, timeWorldCoords.y);
    }

    public void dispose() {
        font.dispose();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrScore() {
        score++;
    }
    
}