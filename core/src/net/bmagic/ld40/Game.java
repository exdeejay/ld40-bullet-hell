package net.bmagic.ld40;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Game extends ApplicationAdapter {

	// Tweakable constants
	public static final float PAUSE_ALPHA = 0.75f;
	// End tweakable constants

	private static Game instance;
	private GameState state;

	private BitmapFont font;
	private GlyphLayout layout;
	private SpriteBatch batch;
	private SpriteBatch overlayBatch;
	private ShapeRenderer shapeRenderer;
	private CameraController cameraController;
	private HUDController hudController;
	private ZombieSpawner zombieSpawner;
	private List<Ammo> ammo;
	private List<Bullet> bullets;
	private List<Zombie> zombies;
	private Player player;
	private Crosshairs crosshairs;
	private float elapsedGameoverTime;
	private Texture logo;
	private Vector3 worldCoords;

	private Texture lamp;

	public Game() {
		instance = this;

		cameraController = new CameraController();
		hudController = new HUDController();
		zombieSpawner = new ZombieSpawner();
		player = new Player();
		crosshairs = new Crosshairs();
	}

	@Override
	public void create() {
		zombies = new ArrayList<Zombie>();
		ammo = new ArrayList<Ammo>();
		bullets = new ArrayList<Bullet>();

		font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
		batch = new SpriteBatch();
		overlayBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		cameraController.init();
		hudController.init();
		zombieSpawner.init();
		Ammo.init();
		Bullet.init();
		player.init();
		Zombie.init();
		crosshairs.init();

		logo = new Texture(Gdx.files.internal("logo.png"));

		lamp = new Texture(Gdx.files.internal("lamp.png"));

		elapsedGameoverTime = 0;

		state = GameState.TITLE;
	}

	@Override
	public void render() {
		// Put game logic here
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		switch (state) {

		case TITLE:
			if (Gdx.input.isTouched()) {
				setState(GameState.RUNNING);
			}

			batch.begin();
			batch.setProjectionMatrix(cameraController.getCamera().combined);
			cameraController.draw(batch);
			batch.end();

			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, PAUSE_ALPHA);
			shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

			overlayBatch.begin();
			overlayBatch.setProjectionMatrix(cameraController.getCamera().combined);
			worldCoords = cameraController.unprojectFromCamera(
				new Vector3(
					cameraController.getCamera().viewportWidth/2 - logo.getWidth()/2,
					cameraController.getCamera().viewportHeight/2 - logo.getHeight()/2 + 100,
					0));
			overlayBatch.draw(
				logo,
				worldCoords.x, worldCoords.y,
				logo.getWidth(),
				logo.getHeight());
			font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			font.getData().setScale(1);
			layout = new GlyphLayout(font, "Click anywhere to start");
			worldCoords = cameraController.unprojectFromCamera(
				new Vector3(
					cameraController.getCamera().viewportWidth/2 - layout.width/2,
					cameraController.getCamera().viewportHeight/2 - layout.height/2 - 100,
					0));
			font.draw(
				overlayBatch,
				"Click anywhere to start",
				worldCoords.x, worldCoords.y);
			overlayBatch.end();
			break;

		case RUNNING:
			if (Gdx.input.isKeyJustPressed(Keys.P)) {
				setState(GameState.PAUSED);
			}

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
			batch.draw(lamp, CameraController.WORLD_WIDTH - lamp.getWidth(),
					CameraController.WORLD_HEIGHT - lamp.getHeight());
			for (int i = 0; i < ammo.size(); i++)
				ammo.get(i).draw(batch);
			for (int i = 0; i < bullets.size(); i++)
				bullets.get(i).draw(batch);
			player.draw(batch);
			for (int i = 0; i < zombies.size(); i++)
				zombies.get(i).draw(batch);
			crosshairs.draw(batch);
			hudController.draw(batch);
			// End sprite drawing
			batch.end();
			break;

		case PAUSED:
			if (Gdx.input.isKeyJustPressed(Keys.P)) {
				setState(GameState.RUNNING);
			}

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
			hudController.draw(batch);
			// End sprite drawing
			batch.end();

			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, PAUSE_ALPHA);
			shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

			overlayBatch.begin();
			overlayBatch.setProjectionMatrix(cameraController.getCamera().combined);
			// Draw HUD here
			font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			font.getData().setScale(2);
			layout = new GlyphLayout(font, "Paused");
			worldCoords = cameraController.unprojectFromCamera(
				new Vector3(
					cameraController.getCamera().viewportWidth/2 - layout.width/2,
					cameraController.getCamera().viewportHeight/2 - layout.height/2 + 100,
					0));
			font.draw(
				overlayBatch,
				"Paused",
				worldCoords.x, worldCoords.y);

			font.getData().setScale(1);
			worldCoords = cameraController.unprojectFromCamera(
				new Vector3(
					cameraController.getCamera().viewportWidth/2 - layout.width/2 - 20,
					cameraController.getCamera().viewportHeight/2 - layout.height/2 - 100,
					0));
			layout = new GlyphLayout(font, "Press P to resume");
			font.draw(
				overlayBatch,
				"Press P to resume",
				worldCoords.x, worldCoords.y);
			// End HUD drawing
			overlayBatch.end();
			break;

		case GAMEOVER:
			if (elapsedGameoverTime > 1 && Gdx.input.isTouched()) {
				create();
				setState(GameState.RUNNING);
			}

			elapsedGameoverTime += Gdx.graphics.getDeltaTime();
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
			hudController.draw(batch);
			// End sprite drawing
			batch.end();

			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			Gdx.gl.glEnable(GL20.GL_BLEND);
			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 0, 0, PAUSE_ALPHA);
			shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);

			overlayBatch.begin();
			overlayBatch.setProjectionMatrix(cameraController.getCamera().combined);
			// Draw HUD here
			font.getRegion().getTexture().setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
			font.getData().setScale(2);
			layout = new GlyphLayout(font, "You died!");
			worldCoords = cameraController.unprojectFromCamera(
				new Vector3(
					cameraController.getCamera().viewportWidth/2 - layout.width/2 - 10,
					cameraController.getCamera().viewportHeight/2 - layout.height/2 + 100,
					0));
			font.draw(
				overlayBatch,
				"You died!",
				worldCoords.x, worldCoords.y);

			font.getData().setScale(1);
			worldCoords = cameraController.unprojectFromCamera(
				new Vector3(
					cameraController.getCamera().viewportWidth/2 - layout.width/2 - 35,
					cameraController.getCamera().viewportHeight/2 - layout.height/2 - 100,
					0));
			layout = new GlyphLayout(font, "Click anywhere to restart");
			font.draw(
				overlayBatch,
				"Click anywhere to restart",
				worldCoords.x, worldCoords.y);
			// End HUD drawing
			overlayBatch.end();
			break;

		default:
			break;
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		overlayBatch.dispose();
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

	public BitmapFont getFont() {
		return font;
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
