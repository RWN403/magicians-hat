package io.github.rwn403;

/**
 * Start the app.
 * @author RWN403
 * @version 1.0.0
 */
public class App {

    private AppController controller;

    private App() {
        controller = new AppController();
    }

    public void start() { controller.launch(); }

    public static void main(String[] args) {
        App app = new App();
        app.start();
    }
}
