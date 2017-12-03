package net.bmagic.ld40;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {

	private static Game instance;

	private SpriteBatch batch;
	private CameraController cameraController;
	private Player player;
	private Crosshairs crosshairs;
	private List<Bullet> bullets;
	private List<Zombie> zombies;

	public Game() {
		instance = this;
		
		cameraController = new CameraController();
		player = new Player();
		crosshairs = new Crosshairs();
		bullets = new ArrayList<Bullet>();
		zombies = new ArrayList<Zombie>();
	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		cameraController.init();
		player.init();
		crosshairs.init();
		Bullet.init();
	}

	@Override
	public void render() {
		// Put game logic here
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}
		player.update();
		crosshairs.update();
		for (int i = 0; i < bullets.size(); i++)
			bullets.get(i).update();
		for (int i = 0; i < zombies.size(); i++)
			zombies.get(i).update();
		// End game logic
		cameraController.update();


		Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setProjectionMatrix(cameraController.getCamera().combined);
		// Draw sprites in here
		cameraController.draw(batch);
		player.draw(batch);
		crosshairs.draw(batch);
		for (Bullet b : bullets)
			b.draw(batch);
		for (Zombie z : zombies)
			z.draw(batch);
		// End sprite drawing
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
		cameraController.dispose();
		player.dispose();
		crosshairs.dispose();
		Bullet.dispose();
	}

	public static Game getInstance() {
		return instance;
	}

	public CameraController getCameraController() {
		return cameraController;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}
}
