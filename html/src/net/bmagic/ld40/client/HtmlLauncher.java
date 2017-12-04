package net.bmagic.ld40.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;

import net.bmagic.ld40.CameraController;
import net.bmagic.ld40.Game;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig() {
                return new GwtApplicationConfiguration(
                        (int) (CameraController.CAMERA_WIDTH * 1.25),
                        (int) (CameraController.CAMERA_HEIGHT * 1.25));
        }

        @Override
        public ApplicationListener createApplicationListener() {
                return new Game();
        }
}