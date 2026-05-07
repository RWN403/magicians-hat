package io.github.rwn403.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Display a GUI scene.
 * @author RWN403
 * @version 1.0.0
 */
class Window extends Application {

    private static final int DEFAULT_WINDOW_HEIGHT = 750;
    private static final int DEFAULT_WINDOW_WIDTH = 1000;

    private Stage stage;

    @Override
    public void start(Stage window) throws Exception {
        window.setHeight(DEFAULT_WINDOW_HEIGHT);
        window.setWidth(DEFAULT_WINDOW_WIDTH);
        // window.setMaximized(true);
        window.setTitle("Magician's Hat");
        window.show();
        stage = window;
    }

    protected void setScene(Scene scene) {
        stage.setScene(scene);
        scene.getRoot().requestFocus();
    }
}
