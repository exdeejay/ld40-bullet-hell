package net.bmagic.ld40.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.bmagic.ld40.CameraController;
import net.bmagic.ld40.Game;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Bullet Hell";
		config.width = (int) (CameraController.CAMERA_WIDTH * 1.5);
		config.height = (int) (CameraController.CAMERA_HEIGHT * 1.5);
		config.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new Game(), config);
	}
}
