package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Display a GUI screen.
 * @author RWN403
 * @version 1.0.0
 */
abstract class Page {
    
    private AppController app;
    private Scene scene;

    protected Page(AppController app) {
        this.app = app;
    }

    public void initialize(Parent content) {
        this.scene = new Scene(content);
    }

    protected Scene getScene() { return scene; }
    protected AppController getAppController() { return app; }
}
