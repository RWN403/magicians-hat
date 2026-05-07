package io.github.rwn403.gui;

import java.util.Optional;

import io.github.rwn403.AppController;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Manage a GUI.
 * @author RWN403
 * @version 1.0.0
 */
public class GuiController {
    
    private Window window;
    private LoginPage login;
    private WorkspacePage workspace;

    public GuiController(AppController app) {
        login = new LoginPage(app);
        workspace = new WorkspacePage(app);
    }

    // Launch the app.
    public void launch() {
        try {
            // Initalize the window.
            window = new Window();
            window.init();
            Platform.startup(() -> {
                Stage stage = new Stage();
                // Handle the window being closed by the user.
                stage.setOnCloseRequest(event -> System.exit(0));
                try {
                    window.start(stage);
                } catch (Exception e) {
                    System.out.println(e);
                    System.exit(0);
                }
                login.initialize();
                workspace.initialize();
                // Start the app on the login page.
                showLogin();
            });
        } catch (Exception e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    // Notify the user of an action.
    public static void notify(String message) { 
        Alert a = new Alert(AlertType.INFORMATION);
        a.setContentText(message);
        a.showAndWait();
    }

    // Notify the user of an error.
    public static void error(String message) {
        Alert a = new Alert(AlertType.ERROR);
        a.setContentText(message);
        a.showAndWait();
    }

    // Request the user to confirm an action.
    public static boolean confirmation(String question) {
        Alert c = new Alert(AlertType.CONFIRMATION);
        c.setContentText(question);
        c.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = c.showAndWait();
        return result.get() == ButtonType.YES;
    }

    public void reset() {
        login.reset();
        workspace.reset();
    }

    public void showLogin() { window.setScene(login.getScene()); }
    public void showWorkspace() { window.setScene(workspace.getScene()); }

    public LoginPage getLoginPage() { return login; }
    public WorkspacePage getWorkspacePage() { return workspace; }
}
