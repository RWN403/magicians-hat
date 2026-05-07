package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import io.github.rwn403.model.IdeaModel;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display all ideas.
 * @author RWN403
 * @version 1.0.0
 */
class AllIdeasTab extends WorkspaceTab {
    
    private static final String TITLE = "Ideas";
    private ListView<IdeaModel> view;
    
    // Create a tab to display all ideas.
    protected AllIdeasTab(AppController app) {
        
        VBox content = new VBox();
        content.setSpacing(5);
        super(TITLE, content, app);

        Label title = new Label(TITLE);

        view = new ListView<>();
        view.setMaxHeight(Double.MAX_VALUE);
        view.setMaxWidth(Double.MAX_VALUE);
        view.setFocusTraversable(false);
        view.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(IdeaModel idea, boolean empty) {
                super.updateItem(idea, empty);
                if (idea != null || !empty) textProperty().bind(Bindings.concat("IDEA#", idea.idProperty()));
                // Open an idea when double-clicked.
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !empty && idea != null) {
                        app.onIdeaListCellDoubleClicked(idea);
                        view.getSelectionModel().clearSelection();
                    }
                });
            }
        });
        VBox.setVgrow(view, Priority.ALWAYS);

        // Set components on the tab.
        content.getChildren().add(title);
        content.getChildren().add(view);
    }

    protected void addIdeas(ObservableList<IdeaModel> ideas) { view.setItems(ideas); }
    protected void removeIdeas() { view.setItems(null); }

    @Override
    protected boolean matches(Object object) {
        return this.equals(object);
    }
}
