package io.github.rwn403.gui;

import io.github.rwn403.AppController;
import io.github.rwn403.model.DraftModel;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * Display all drafts.
 * @author RWN403
 * @version 1.0.0
 */
class AllDraftsTab extends WorkspaceTab {
    
    private static final String TITLE = "Drafts";
    private ListView<DraftModel> view;

    // Create a tab to display all drafts.
    protected AllDraftsTab(AppController app) {
        
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
            protected void updateItem(DraftModel draft, boolean empty) {
                super.updateItem(draft, empty);
                if (draft != null || !empty) textProperty().bind(draft.titleProperty());
                // Open a draft when double-clicked.
                setOnMouseClicked(event -> {
                    if (event.getClickCount() == 2 && !empty && draft != null) {
                        app.onDraftListCellDoubleClicked(draft);
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

    protected void addDrafts(ObservableList<DraftModel> drafts) { view.setItems(drafts); }
    protected void removeDrafts() { view.setItems(null); }

    @Override
    protected boolean matches(Object object) {
        return this.equals(object);
    }
}
