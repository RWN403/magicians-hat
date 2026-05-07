package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display content in a workspace.
 * @author RWN403
 * @version 1.0.0
 */
abstract class WorkspaceTab extends Tab {

    private AppController app;

    // Create a workspace tab.
    protected WorkspaceTab(String title, Node content, AppController app) {
        this.app = app;
        VBox margins = new VBox();
        margins.getChildren().add(content);
        super(title, margins);
        // Add insets to tab contents.
        VBox.setMargin(content, (new Insets(10, 10, 10, 10)));
        VBox.setVgrow(content, Priority.ALWAYS);
    }

    // Set the visibility of a tab component.
    protected void setVisible(Node component, boolean value) {
        component.setVisible(value);
        component.setManaged(value);
    }

    protected AppController getAppController() { return app; }
    abstract protected boolean matches(Object object);
}
