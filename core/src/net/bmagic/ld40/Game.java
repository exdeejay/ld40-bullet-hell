package net.bmagic.ld40;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {

	private static Game instance;
	private GameState state;

	private SpriteBatch batch;
	private SpriteBatch hudBatch;
	private CameraController cameraController;
	private HUDController hudController;
	private ZombieSpawner zombieSpawner;
	private List<Ammo> ammo;
	private List<Bullet> bullets;
	private List<Zombie> zombies;
	private Player player;
	private Crosshairs crosshairs;

	private Texture lamp;

	public Game() {
		instance = this;

		cameraController = new CameraController();
		hudController = new HUDController();
		zombieSpawner = new ZombieSpawner();
		ammo = new ArrayList<Ammo>();
		bullets = new ArrayList<Bullet>();
		player = new Player();
		zombies = new ArrayList<Zombie>();
		crosshairs = new Crosshairs();

	}
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		hudBatch = new SpriteBatch();
		cameraController.init();
		hudController.init();
		zombieSpawner.init();
		Ammo.init();
		Bullet.init();
		player.init();
		Zombie.init();
		crosshairs.init();
		
		lamp = new Texture(Gdx.files.internal("lamp.png"));
		
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
			for (int i = 0; i < ammo.size(); i++)
				ammo.get(i).update();
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).update();
			player.update();
			for (int i = 0; i < zombies.size(); i++)
				zombies.get(i).update();
			crosshairs.update();
			zombieSpawner.update();
			hudController.update();
			// End game logic
			cameraController.update();

			Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.begin();
			batch.setProjectionMatrix(cameraController.getCamera().combined);
			// Draw sprites in here
			cameraController.draw(batch);
			batch.draw(lamp, 0, 0);
			for (int i = 0; i < ammo.size(); i++)
				ammo.get(i).draw(batch);
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).draw(batch);
			player.draw(batch);
			for (int i = 0; i < zombies.size(); i++)
				zombies.get(i).draw(batch);
			crosshairs.draw(batch);
			// End sprite drawing
			batch.end();

			hudBatch.begin();
			// Draw HUD here
			hudController.draw(hudBatch);
			// End HUD drawing
			hudBatch.end();
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
			for (Ammo a : ammo)
				a.draw(batch);
			for (Bullet b : bullets)
				b.draw(batch);
			player.draw(batch);
			for (Zombie z : zombies)
				z.draw(batch);
			crosshairs.draw(batch);
			// End sprite drawing
			batch.end();
			
			hudBatch.begin();
			// Draw HUD here
			hudController.draw(hudBatch);
			// End HUD drawing
			hudBatch.end();
			break;

		default:
			break;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		hudBatch.dispose();
		cameraController.dispose();
		Bullet.dispose();
		Zombie.dispose();
		player.dispose();
		crosshairs.dispose();

		lamp.dispose();
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

	public HUDController getHUDController() {
		return hudController;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Ammo> getAmmo() {
		return ammo;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public List<Zombie> getZombies() {
		return zombies;
	}
}
