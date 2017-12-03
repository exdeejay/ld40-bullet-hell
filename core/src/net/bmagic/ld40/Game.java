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
	private GameState state;

	private SpriteBatch batch;
	private CameraController cameraController;
	private ZombieSpawner zombieSpawner;
	private List<Bullet> bullets;
	private List<Zombie> zombies;
	private Player player;
	private Crosshairs crosshairs;

	public Game() {
		instance = this;

		cameraController = new CameraController();
		zombieSpawner = new ZombieSpawner();
		bullets = new ArrayList<Bullet>();
		zombies = new ArrayList<Zombie>();
		player = new Player();
		crosshairs = new Crosshairs();
	}

	@Override
	public void create() {
		batch = new SpriteBatch();
		cameraController.init();
		zombieSpawner.init();
		Bullet.init();
		Zombie.init();
		player.init();
		crosshairs.init();

		state = GameState.RUNNING;
	}

	@Override
	public void render() {
		// Put game logic here
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		switch (state) {
		case RUNNING:
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).update();
			for (int i = 0; i < zombies.size(); i++)
				zombies.get(i).update();
			player.update();
			crosshairs.update();
			zombieSpawner.update();
			// End game logic
			cameraController.update();

			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.setProjectionMatrix(cameraController.getCamera().combined);
			// Draw sprites in here
			cameraController.draw(batch);
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).draw(batch);
			for (int i = 0; i < zombies.size(); i++)
				zombies.get(i).draw(batch);
			player.draw(batch);
			crosshairs.draw(batch);
			// End sprite drawing
			batch.end();
			break;

		case PAUSED:
			break;

		case GAMEOVER:
			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.setProjectionMatrix(cameraController.getCamera().combined);
			// Draw sprites in here
			cameraController.draw(batch);
			for (Bullet b : bullets)
				b.draw(batch);
			for (Zombie z : zombies)
				z.draw(batch);
			player.draw(batch);
			crosshairs.draw(batch);
			// End sprite drawing
			batch.end();
			break;

		default:
			break;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		cameraController.dispose();
		Bullet.dispose();
		Zombie.dispose();
		player.dispose();
		crosshairs.dispose();
	}

	public static Game getInstance() {
		return instance;
	}

	public void setState(GameState state) {
		this.state = state;
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

	public List<Zombie> getZombies() {
		return zombies;
	}
}
